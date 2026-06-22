import axios from 'axios'

const BASE = '/api/images'

export const api = {
  upload(file) {
    const form = new FormData()
    form.append('file', file)
    return axios.post(`${BASE}/upload`, form)
  },

  downloadUrl(imageId) {
    return `${BASE}/${imageId}/download`
  },

  grayscale(imageId) {
    return axios.post(`${BASE}/${imageId}/grayscale`)
  },

  brightness(imageId, value) {
    return axios.post(`${BASE}/${imageId}/brightness`, null, { params: { value } })
  },

  contrast(imageId, factor) {
    return axios.post(`${BASE}/${imageId}/contrast`, null, { params: { factor } })
  },

  horizontalFlip(imageId) {
    return axios.post(`${BASE}/${imageId}/horizontal-flip`)
  },

  verticalFlip(imageId) {
    return axios.post(`${BASE}/${imageId}/vertical-flip`)
  },

  rotate(imageId, angle) {
    return axios.post(`${BASE}/${imageId}/rotate`, null, { params: { angle } })
  },

  zoom(imageId, factor) {
    return axios.post(`${BASE}/${imageId}/zoom`, null, { params: { factor } })
  },

  blur(imageId) {
    return axios.post(`${BASE}/${imageId}/blur`)
  },

  sharpen(imageId) {
    return axios.post(`${BASE}/${imageId}/sharpen`)
  },

  layer(imageId, overlayImage, x, y) {
    const form = new FormData()
    form.append('overlayImage', overlayImage)
    form.append('x', x)
    form.append('y', y)
    return axios.post(`${BASE}/${imageId}/layer`, form)
  },

  removeBackground(imageId, threshold = 40) {
    return axios.post(`${BASE}/${imageId}/remove-background`, null, { params: { threshold } })
  },

  detectShapes(imageId) {
    return axios.post(`${BASE}/${imageId}/detect-shapes`)
  },

  threshold(imageId) {
    return axios.post(`${BASE}/${imageId}/threshold`)
  },
}