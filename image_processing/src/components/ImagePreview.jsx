import styles from './ImagePreview.module.css'
import { api } from '../utils/api'

export default function ImagePreview({ imageId, previewUrl, fileName, onReset }) {
  const downloadUrl = api.downloadUrl(imageId)

  return (
    <div className={styles.wrapper}>
      <div className={styles.header}>
        <div className={styles.fileinfo}>
          <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
            <rect x="3" y="3" width="18" height="18" rx="2" />
            <circle cx="8.5" cy="8.5" r="1.5" />
            <polyline points="21 15 16 10 5 21" />
          </svg>
          <span className={styles.filename}>{fileName}</span>
          <span className={styles.idpill}>
            <span className="mono">{imageId.slice(0, 8)}…</span>
          </span>
        </div>
        <div className={styles.actions}>
          <a
            href={downloadUrl}
            download
            className={styles.downloadBtn}
          >
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4" />
              <polyline points="7 10 12 15 17 10" />
              <line x1="12" y1="15" x2="12" y2="3" />
            </svg>
            Download
          </a>
          <button className={styles.resetBtn} onClick={onReset}>
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" aria-hidden="true">
              <polyline points="1 4 1 10 7 10" />
              <path d="M3.51 15a9 9 0 1 0 .49-4.02" />
            </svg>
            New image
          </button>
        </div>
      </div>

      <div className={styles.canvas}>
        <div className={styles.pane}>
          <span className={styles.paneLabel}>Current</span>
          <img
            key={previewUrl}
            src={previewUrl}
            alt="Current image"
            className={styles.img}
          />
        </div>
      </div>
    </div>
  )
}