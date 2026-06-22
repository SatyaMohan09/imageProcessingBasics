import { useState, useRef } from 'react'
import { api } from '../utils/api'
import styles from './UploadZone.module.css'

export default function UploadZone({ onUploaded }) {
  const [dragging, setDragging] = useState(false)
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)
  const inputRef = useRef()

  async function handleFile(file) {
    if (!file) return
    if (!file.type.startsWith('image/')) {
      setError('Please upload an image file (JPG, PNG, WEBP, etc.)')
      return
    }
    setError(null)
    setLoading(true)
    try {
      const res = await api.upload(file)
      const text = res.data
      const idMatch = text.match(/ID = ([a-f0-9-]+)/i)
      const imageId = idMatch ? idMatch[1] : null
      if (!imageId) throw new Error('Could not parse image ID from response')
      const previewUrl = URL.createObjectURL(file)
      onUploaded(imageId, previewUrl, file.name)
    } catch (e) {
      setError(e.response?.data || e.message || 'Upload failed')
    } finally {
      setLoading(false)
    }
  }

  function onDrop(e) {
    e.preventDefault()
    setDragging(false)
    handleFile(e.dataTransfer.files[0])
  }

  return (
    <div className={styles.wrapper}>
      <div
        className={`${styles.zone} ${dragging ? styles.dragging : ''}`}
        onDragOver={e => { e.preventDefault(); setDragging(true) }}
        onDragLeave={() => setDragging(false)}
        onDrop={onDrop}
        onClick={() => inputRef.current.click()}
      >
        <input
          ref={inputRef}
          type="file"
          accept="image/*"
          style={{ display: 'none' }}
          onChange={e => handleFile(e.target.files[0])}
        />
        {loading ? (
          <div className={styles.loading}>
            <div className={styles.spinner} />
            <span>Uploading…</span>
          </div>
        ) : (
          <>
            <div className={styles.icon}>
              <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
                <rect x="3" y="3" width="18" height="18" rx="2" />
                <circle cx="8.5" cy="8.5" r="1.5" />
                <polyline points="21 15 16 10 5 21" />
              </svg>
            </div>
            <p className={styles.label}>Drop an image here</p>
            <p className={styles.sub}>or click to browse · JPG, PNG, WEBP, GIF</p>
          </>
        )}
      </div>
      {error && <p className={styles.error}>{error}</p>}
    </div>
  )
}