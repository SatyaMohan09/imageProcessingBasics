import { useState, useRef } from 'react'
import { api } from '../utils/api'
import styles from './OperationsPanel.module.css'

function Section({ title, children }) {
  return (
    <div className={styles.section}>
      <h3 className={styles.sectionTitle}>{title}</h3>
      <div className={styles.sectionBody}>{children}</div>
    </div>
  )
}

function OpButton({ label, onClick, loading, icon }) {
  return (
    <button
      className={styles.opBtn}
      onClick={onClick}
      disabled={loading}
      aria-label={label}
    >
      {loading ? <span className={styles.mini_spinner} /> : <span className={styles.btnIcon}>{icon}</span>}
      {label}
    </button>
  )
}

export default function OperationsPanel({ imageId, onResult }) {
  const [busy, setBusy] = useState(null)
  const [brightness, setBrightness] = useState(30)
  const [contrast, setContrast] = useState(1.5)
  const [zoom, setZoom] = useState(1.5)
  const [angle, setAngle] = useState(90)
  const [bgThreshold, setBgThreshold] = useState(40)
  const [layerX, setLayerX] = useState(0)
  const [layerY, setLayerY] = useState(0)
  const overlayRef = useRef()
  const [shapes, setShapes] = useState(null)

  async function run(key, apiFn) {
    setBusy(key)
    setShapes(null)
    try {
      const res = await apiFn()
      if (key === 'detect-shapes') {
        setShapes(res.data)
      }
      onResult({ success: true, key })
    } catch (e) {
      onResult({ success: false, key, error: e.response?.data || e.message })
    } finally {
      setBusy(null)
    }
  }

  const b = (key) => busy === key

  return (
    <div className={styles.panel}>

      <Section title="Adjustments">
        <div className={styles.sliderRow}>
          <label className={styles.sliderLabel}>
            Brightness
            <span className={styles.sliderVal}>{brightness > 0 ? `+${brightness}` : brightness}</span>
          </label>
          <input
            type="range" min="-100" max="100" step="5"
            value={brightness}
            onChange={e => setBrightness(Number(e.target.value))}
            className={styles.slider}
          />
          <button
            className={styles.applyBtn}
            onClick={() => run('brightness', () => api.brightness(imageId, brightness))}
            disabled={busy === 'brightness'}
          >
            {b('brightness') ? <span className={styles.mini_spinner} /> : 'Apply'}
          </button>
        </div>

        <div className={styles.sliderRow}>
          <label className={styles.sliderLabel}>
            Contrast
            <span className={styles.sliderVal}>{contrast.toFixed(1)}×</span>
          </label>
          <input
            type="range" min="0.1" max="3" step="0.1"
            value={contrast}
            onChange={e => setContrast(Number(e.target.value))}
            className={styles.slider}
          />
          <button
            className={styles.applyBtn}
            onClick={() => run('contrast', () => api.contrast(imageId, contrast))}
            disabled={busy === 'contrast'}
          >
            {b('contrast') ? <span className={styles.mini_spinner} /> : 'Apply'}
          </button>
        </div>
      </Section>

      <Section title="Filters">
        <div className={styles.btnGrid}>
          <OpButton label="Grayscale" loading={b('grayscale')}
            onClick={() => run('grayscale', () => api.grayscale(imageId))}
            icon={<GrayIcon />} />
          <OpButton label="Blur" loading={b('blur')}
            onClick={() => run('blur', () => api.blur(imageId))}
            icon={<BlurIcon />} />
          <OpButton label="Sharpen" loading={b('sharpen')}
            onClick={() => run('sharpen', () => api.sharpen(imageId))}
            icon={<SharpenIcon />} />
          <OpButton label="Threshold" loading={b('threshold')}
            onClick={() => run('threshold', () => api.threshold(imageId))}
            icon={<ThreshIcon />} />
        </div>
      </Section>

      <Section title="Transform">
        <div className={styles.btnGrid}>
          <OpButton label="Flip H" loading={b('h-flip')}
            onClick={() => run('h-flip', () => api.horizontalFlip(imageId))}
            icon={<FlipHIcon />} />
          <OpButton label="Flip V" loading={b('v-flip')}
            onClick={() => run('v-flip', () => api.verticalFlip(imageId))}
            icon={<FlipVIcon />} />
        </div>

        <div className={styles.sliderRow} style={{ marginTop: 10 }}>
          <label className={styles.sliderLabel}>
            Rotate
            <span className={styles.sliderVal}>{angle}°</span>
          </label>
          <div className={styles.segmented}>
            {[90, 180, 270].map(a => (
              <button
                key={a}
                className={`${styles.seg} ${angle === a ? styles.segActive : ''}`}
                onClick={() => setAngle(a)}
              >{a}°</button>
            ))}
          </div>
          <button
            className={styles.applyBtn}
            onClick={() => run('rotate', () => api.rotate(imageId, angle))}
            disabled={b('rotate')}
          >
            {b('rotate') ? <span className={styles.mini_spinner} /> : 'Rotate'}
          </button>
        </div>

        <div className={styles.sliderRow} style={{ marginTop: 10 }}>
          <label className={styles.sliderLabel}>
            Zoom
            <span className={styles.sliderVal}>{zoom.toFixed(1)}×</span>
          </label>
          <input
            type="range" min="0.2" max="4" step="0.1"
            value={zoom}
            onChange={e => setZoom(Number(e.target.value))}
            className={styles.slider}
          />
          <button
            className={styles.applyBtn}
            onClick={() => run('zoom', () => api.zoom(imageId, zoom))}
            disabled={b('zoom')}
          >
            {b('zoom') ? <span className={styles.mini_spinner} /> : 'Apply'}
          </button>
        </div>
      </Section>

      <Section title="Background removal">
        <div className={styles.sliderRow}>
          <label className={styles.sliderLabel}>
            Threshold
            <span className={styles.sliderVal}>{bgThreshold}</span>
          </label>
          <input
            type="range" min="5" max="120" step="5"
            value={bgThreshold}
            onChange={e => setBgThreshold(Number(e.target.value))}
            className={styles.slider}
          />
          <button
            className={styles.applyBtn}
            onClick={() => run('remove-bg', () => api.removeBackground(imageId, bgThreshold))}
            disabled={b('remove-bg')}
          >
            {b('remove-bg') ? <span className={styles.mini_spinner} /> : 'Remove'}
          </button>
        </div>
      </Section>

      <Section title="Detect shapes">
        <button
          className={styles.fullBtn}
          onClick={() => run('detect-shapes', () => api.detectShapes(imageId))}
          disabled={b('detect-shapes')}
        >
          {b('detect-shapes') ? <><span className={styles.mini_spinner} /> Detecting…</> : 'Run shape detection'}
        </button>
        {shapes && shapes.length > 0 && (
          <div className={styles.shapesTable}>
            <table>
              <thead>
                <tr>
                  <th>Shape</th>
                  <th>Area</th>
                  <th>Width</th>
                  <th>Height</th>
                </tr>
              </thead>
              <tbody>
                {shapes.map((s, i) => (
                  <tr key={i}>
                    <td><span className={styles.shapeBadge}>{s.shape}</span></td>
                    <td>{s.area.toLocaleString()}</td>
                    <td>{s.width}px</td>
                    <td>{s.height}px</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
        {shapes && shapes.length === 0 && (
          <p className={styles.noShapes}>No shapes detected in this image.</p>
        )}
      </Section>

      <Section title="Layer overlay">
        <div className={styles.layerGrid}>
          <div className={styles.layerField}>
            <label className={styles.fieldLabel}>Overlay image</label>
            <input
              ref={overlayRef}
              type="file"
              accept="image/*"
              className={styles.fileInput}
            />
          </div>
          <div className={styles.xyRow}>
            <div className={styles.layerField}>
              <label className={styles.fieldLabel}>X offset</label>
              <input
                type="number" value={layerX}
                onChange={e => setLayerX(Number(e.target.value))}
                className={styles.numInput}
              />
            </div>
            <div className={styles.layerField}>
              <label className={styles.fieldLabel}>Y offset</label>
              <input
                type="number" value={layerY}
                onChange={e => setLayerY(Number(e.target.value))}
                className={styles.numInput}
              />
            </div>
          </div>
        </div>
        <button
          className={styles.fullBtn}
          style={{ marginTop: 10 }}
          onClick={() => {
            const file = overlayRef.current?.files?.[0]
            if (!file) return
            run('layer', () => api.layer(imageId, file, layerX, layerY))
          }}
          disabled={b('layer')}
        >
          {b('layer') ? <><span className={styles.mini_spinner} /> Applying…</> : 'Apply layer'}
        </button>
      </Section>

    </div>
  )
}

function GrayIcon() {
  return <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="9"/><path d="M12 3a9 9 0 0 1 0 18"/></svg>
}
function BlurIcon() {
  return <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><circle cx="12" cy="12" r="2"/><path d="M12 2v4M12 18v4M4.93 4.93l2.83 2.83M16.24 16.24l2.83 2.83M2 12h4M18 12h4M4.93 19.07l2.83-2.83M16.24 7.76l2.83-2.83"/></svg>
}
function SharpenIcon() {
  return <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><polygon points="12 2 15.09 8.26 22 9.27 17 14.14 18.18 21.02 12 17.77 5.82 21.02 7 14.14 2 9.27 8.91 8.26 12 2"/></svg>
}
function ThreshIcon() {
  return <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><rect x="3" y="3" width="8" height="8" fill="currentColor"/><rect x="13" y="3" width="8" height="8"/><rect x="3" y="13" width="8" height="8"/><rect x="13" y="13" width="8" height="8" fill="currentColor"/></svg>
}
function FlipHIcon() {
  return <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M12 3v18M4 9l-3 3 3 3M20 9l3 3-3 3"/></svg>
}
function FlipVIcon() {
  return <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round"><path d="M3 12h18M9 4l3-3 3 3M9 20l3 3 3-3"/></svg>
}