"use client"

import React, { useState } from "react"
import { QrCode, X, Download, Smartphone, Zap } from "lucide-react"

const DIRECT_APK_URL = "https://github.com/alexansh/VitalSense/releases/download/version/app-debug.apk"
const RELEASE_PAGE_URL = "https://github.com/alexansh/VitalSense/releases/tag/version"

export function QrDownloadModal() {
  const [isOpen, setIsOpen] = useState(false)

  return (
    <>
      {/* Trigger Button */}
      <button
        type="button"
        onClick={() => setIsOpen(true)}
        className="flex min-h-[56px] w-full items-center justify-center gap-2 rounded-xl border border-[#2E9E5B]/40 bg-white px-6 text-base font-bold text-[#1C1C1C] shadow-xs transition hover:bg-[#E8F7EE] active:scale-[0.98] sm:w-auto"
      >
        <QrCode className="h-5 w-5 text-[#2E9E5B]" />
        <span>📷 Scan QR to Install APK</span>
      </button>

      {/* Modal Backdrop & Dialog */}
      {isOpen && (
        <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/60 p-4 backdrop-blur-sm animate-in fade-in">
          <div className="relative w-full max-w-md rounded-3xl border border-black/10 bg-white p-6 text-center shadow-2xl">
            {/* Close Button */}
            <button
              type="button"
              onClick={() => setIsOpen(false)}
              className="absolute top-4 right-4 flex h-9 w-9 items-center justify-center rounded-full bg-black/5 text-black/60 hover:bg-black/10 transition"
              aria-label="Close"
            >
              <X className="h-5 w-5" />
            </button>

            {/* Header */}
            <div className="mb-2 inline-flex items-center gap-1.5 rounded-full bg-[#E8F7EE] px-3 py-1 text-xs font-bold text-[#2E9E5B]">
              <span>🛡️ Smart India Hackathon (SIH 26133)</span>
            </div>
            <h3 className="text-2xl font-extrabold text-[#1C1C1C]">Scan to Download APK</h3>
            <p className="mt-1 text-xs text-[#1C1C1C]/70">
              Point your phone camera — APK download begins immediately
            </p>

            {/* QR Code Container */}
            <div className="my-4 flex flex-col items-center justify-center">
              <div className="relative rounded-2xl border-2 border-dashed border-[#2E9E5B] bg-[#FAFAF7] p-3.5 shadow-inner">
                {/* eslint-disable-next-line @next/next/no-img-element */}
                <img
                  src="/vitalsense_download_qr.png"
                  alt="Scan QR to Download VitalSense APK Immediately"
                  className="h-56 w-56 rounded-xl object-contain"
                />
                <span className="absolute -bottom-3 left-1/2 -translate-x-1/2 rounded-full bg-[#2E9E5B] px-3 py-0.5 text-[11px] font-extrabold tracking-wider text-white uppercase shadow-sm whitespace-nowrap">
                  Instant APK Download
                </span>
              </div>
            </div>

            {/* Quick Steps */}
            <div className="rounded-xl bg-[#FAFAF7] p-3 text-left text-xs text-[#1C1C1C]/80 mt-2">
              <div className="flex items-center gap-1.5 font-bold text-[#2E9E5B] mb-1">
                <Zap className="h-3.5 w-3.5 fill-[#2E9E5B]" /> Automatic Download:
              </div>
              <p>• Point camera at the QR code above</p>
              <p>• Browser opens and immediately downloads <strong>app-debug.apk</strong></p>
              <p>• Tap downloaded APK & install on your Android device</p>
            </div>

            {/* Direct Links */}
            <div className="mt-4 flex flex-col gap-2">
              <a
                href={DIRECT_APK_URL}
                className="flex items-center justify-center gap-2 rounded-xl bg-[#2E9E5B] py-3 text-sm font-bold text-white transition hover:bg-[#25834b]"
              >
                <Download className="h-4 w-4" />
                <span>Download APK Directly (67 MB)</span>
              </a>
              <a
                href={RELEASE_PAGE_URL}
                target="_blank"
                rel="noopener noreferrer"
                className="flex items-center justify-center gap-1.5 rounded-xl border border-black/10 py-2.5 text-xs font-semibold text-[#1C1C1C]/70 hover:bg-black/5"
              >
                <span>View Release on GitHub</span>
              </a>
            </div>
          </div>
        </div>
      )}
    </>
  )
}
