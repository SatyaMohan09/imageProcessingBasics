import { useState } from 'react'
import UploadZone from './components/UploadZone'
import ImagePreview from './components/ImagePreview'
import OperationsPanel from './components/OperationsPanel'
import styles from './App.module.css'

export default function App() {
  const [image, setImage] = useState(null)
  const [toast, setToast] = useState(null)

  function handleUploaded(imageId, previewUrl, fileName) {
    setImage({ imageId, previewUrl, fileName })
    showToast('Image uploaded', 'success')
  }

  function handleResult({ success, key, error }) {
    if (success) {
      if (key !== 'detect-shapes') {
        // Cache-bust the download URL so the browser re-fetches the latest image
        setImage(prev => ({
          ...prev,
          previewUrl: `/api/images/${prev.imageId}/download?t=${Date.now()}`,
        }))
      }
      showToast(`${formatKey(key)} applied`, 'success')
    } else {
      showToast(error || 'Operation failed', 'error')
    }
  }

  function showToast(message, type) {
    setToast({ message, type })
    setTimeout(() => setToast(null), 3500)
  }

  function formatKey(key) {
    return key
      .replace(/-/g, ' ')
      .replace(/\b\w/g, c => c.toUpperCase())
  }

  return (
    <div className={styles.app}>
      <header className={styles.header}>
        <div className={styles.logo}>
          <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.75" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
            <rect x="3" y="3" width="18" height="18" rx="2" />
            <circle cx="8.5" cy="8.5" r="1.5" />
            <polyline points="21 15 16 10 5 21" />
          </svg>
          <span className={styles.logoName}>Pixly</span>
        </div>
        <span className={styles.tagline}>Image Processor</span>
      </header>

      <main className={styles.main}>
        {!image ? (
          <div className={styles.uploadView}>
            <div className={styles.uploadHero}>
              <h1 className={styles.heroTitle}>Process your images</h1>
              <p className={styles.heroSub}>
                Upload an image to apply filters, transforms, and more — all processed by your Spring Boot backend.
              </p>
            </div>
            <UploadZone onUploaded={handleUploaded} />
          </div>
        ) : (
          <div className={styles.workspaceView}>
            <div className={styles.previewCol}>
              <ImagePreview
                imageId={image.imageId}
                previewUrl={image.previewUrl}
                fileName={image.fileName}
                onReset={() => setImage(null)}
              />
            </div>
            <aside className={styles.opsCol}>
              <OperationsPanel
                imageId={image.imageId}
                onResult={handleResult}
              />
            </aside>
          </div>
        )}
      </main>

      {toast && (
        <div className={`${styles.toast} ${styles[toast.type]}`}>
          {toast.type === 'success' ? (
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <polyline points="20 6 9 17 4 12" />
            </svg>
          ) : (
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <circle cx="12" cy="12" r="10" />
              <line x1="12" y1="8" x2="12" y2="12" />
              <line x1="12" y1="16" x2="12.01" y2="16" />
            </svg>
          )}
          {toast.message}
        </div>
      )}
    </div>
  )
}