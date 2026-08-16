import React from 'react'

/**
 * Signature UI element: every record card is stamped with the microservice
 * (and port) that actually owns that data — a literal nod to this being a
 * multi-service system, not one monolithic backend.
 */
export default function ServiceStamp({ service, port }) {
  return (
    <div className="service-stamp">
      <span className="stamp-dot" />
      {service}<span className="stamp-port">:{port}</span>
    </div>
  )
}
