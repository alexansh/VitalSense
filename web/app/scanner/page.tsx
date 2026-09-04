"use client"

import React, { useState, useEffect, useRef } from "react"
import Link from "next/link"
import { ArrowLeft, Camera, QrCode, Download, ExternalLink, RefreshCw } from "lucide-react"

export default function ScannerPage() {
  const [activeTab, setActiveTab] = useState<"scan" | "show">("scan")
  const [scanResult, setScanResult] = useState<string | null>(null)
  const [cameraError, setCameraError] = useState<string | null>(null)
  const videoRef = useRef<HTMLVideoElement | null>(null)
  const canvasRef = useRef<HTMLCanvasElement | null>(null)
  const streamRef = useRef<MediaStream | null>(null)

  useEffect(() => {
    if (activeTab === "scan") {
      startCamera()
    } else {
      stopCamera()
    }

    return () => {
      stopCamera()
    }
  }, [activeTab])

  const startCamera = async () => {
    setCameraError(null)
    setScanResult(null)
    try {
      const stream = await navigator.mediaDevices.getUserMedia({
        video: { facingMode: "environment" }
      })
      streamRef.current = stream
      if (videoRef.current) {
        videoRef.current.srcObject = stream
        videoRef.current.setAttribute("playsinline", "true")
        await videoRef.current.play()
        requestAnimationFrame(tick)
      }
    } catch (err: any) {
      console.error("Camera error:", err)
      setCameraError(err.message || "Camera permission denied or camera not found.")
    }
  }

  const stopCamera = () => {
    if (streamRef.current) {
      streamRef.current.getTracks().forEach((track) => track.stop())
      streamRef.current = null
    }
  }

  const tick = () => {
    if (!videoRef.current || !canvasRef.current || activeTab !== "scan") return

    const video = videoRef.current
    const canvas = canvasRef.current
    const ctx = canvas.getContext("2d")

    if (video.readyState === video.HAVE_ENOUGH_DATA && ctx) {
      canvas.height = video.videoHeight
      canvas.width = video.videoWidth
      ctx.drawImage(video, 0, 0, canvas.width, canvas.height)

      // Use modern BarcodeDetector if available in browser
      if ("BarcodeDetector" in window) {
        // @ts-ignore
        const barcodeDetector = new window.BarcodeDetector({ formats: ["qr_code"] })
        barcodeDetector
          .detect(canvas)
          .then((barcodes: any[]) => {
            if (barcodes.length > 0) {
              handleDetected(barcodes[0].rawValue)
              return
            }
          })
          .catch(() => {})
      }
    }

    requestAnimationFrame(tick)
  }

  const handleDetected = (rawValue: string) => {
    setScanResult(rawValue)
    stopCamera()
  }

  return (
    <main className="min-h-screen w-full bg-[#FAFAF7] text-[#1C1C1C] px-4 py-8 flex flex-col items-center">
      {/* Top Header */}
      <div className="w-full max-w-md flex items-center justify-between mb-6">
        <Link
          href="/"
          className="flex items-center gap-1.5 text-sm font-bold text-[#1C1C1C]/70 hover:text-[#2E9E5B] transition"
        >
          <ArrowLeft className="h-4 w-4" /> Back to Home
        </Link>
        <span className="text-xs font-extrabold uppercase bg-[#E8F7EE] text-[#2E9E5B] px-3 py-1 rounded-full border border-[#2E9E5B]/20">
          SIH 26133
        </span>
      </div>

      {/* Main Card */}
      <div className="w-full max-w-md rounded-3xl bg-white border border-black/8 p-6 shadow-sm text-center">
        <h1 className="text-2xl font-extrabold text-[#1C1C1C]">VitalSense QR Hub</h1>
        <p className="text-xs text-[#1C1C1C]/60 mt-1 mb-5">
          Scan QR to download the APK or display the scannable code
        </p>

        {/* Tab Switch */}
        <div className="flex bg-[#F0F0EA] p-1 rounded-xl mb-6">
          <button
            type="button"
            onClick={() => setActiveTab("scan")}
            className={`flex-1 py-2 text-xs font-bold rounded-lg transition flex items-center justify-center gap-1.5 ${
              activeTab === "scan"
                ? "bg-white text-[#1C1C1C] shadow-xs"
                : "text-[#1C1C1C]/60 hover:text-[#1C1C1C]"
            }`}
          >
            <Camera className="h-3.5 w-3.5 text-[#2E9E5B]" /> Live Scanner
          </button>
          <button
            type="button"
            onClick={() => setActiveTab("show")}
            className={`flex-1 py-2 text-xs font-bold rounded-lg transition flex items-center justify-center gap-1.5 ${
              activeTab === "show"
                ? "bg-white text-[#1C1C1C] shadow-xs"
                : "text-[#1C1C1C]/60 hover:text-[#1C1C1C]"
            }`}
          >
            <QrCode className="h-3.5 w-3.5 text-[#2E9E5B]" /> Display Download QR
          </button>
        </div>

        {/* Tab 1: Live Scanner */}
        {activeTab === "scan" && (
          <div>
            <div className="relative w-full aspect-square bg-black rounded-2xl overflow-hidden border-2 border-[#2E9E5B] flex items-center justify-center">
              <video ref={videoRef} playsInline autoPlay muted className="w-full h-full object-cover" />
              <canvas ref={canvasRef} className="hidden" />

              {/* Viewfinder Overlay */}
              <div className="absolute w-3/4 h-3/4 border-2 border-white/80 rounded-2xl pointer-events-none shadow-[0_0_0_9999px_rgba(0,0,0,0.45)]">
                <div className="w-full h-0.5 bg-[#2E9E5B] shadow-[0_0_8px_#2E9E5B] animate-pulse" />
              </div>

              {cameraError && (
                <div className="absolute inset-0 bg-black/80 flex flex-col items-center justify-center p-4 text-white">
                  <p className="text-xs font-semibold text-red-400 mb-2">Camera Unavailable</p>
                  <p className="text-[11px] opacity-70 mb-3">{cameraError}</p>
                  <button
                    type="button"
                    onClick={startCamera}
                    className="flex items-center gap-1 bg-white/20 px-3 py-1.5 rounded-lg text-xs font-bold"
                  >
                    <RefreshCw className="h-3 w-3" /> Retry
                  </button>
                </div>
              )}
            </div>

            {/* Scan Detection Result */}
            {scanResult && (
              <div className="mt-4 p-4 rounded-2xl bg-[#E8F7EE] border border-[#2E9E5B]/30 text-left animate-in fade-in">
                <div className="text-xs font-extrabold text-[#2E9E5B] mb-1">
                  ✓ QR Code Scanned!
                </div>
                <div className="text-xs text-black/80 font-mono bg-white p-2 rounded-lg border border-black/5 break-all mb-3">
                  {scanResult}
                </div>
                <a
                  href={scanResult.startsWith("http") ? scanResult : "https://github.com/alexansh/vitalsense"}
                  target="_blank"
                  rel="noopener noreferrer"
                  className="flex items-center justify-center gap-2 w-full py-3 rounded-xl bg-[#2E9E5B] text-white text-xs font-extrabold hover:bg-[#25834b] transition"
                >
                  <Download className="h-4 w-4" />
                  <span>Download VitalSense APK</span>
                  <ExternalLink className="h-3 w-3 opacity-70" />
                </a>
              </div>
            )}
          </div>
        )}

        {/* Tab 2: Display Download QR */}
        {activeTab === "show" && (
          <div className="flex flex-col items-center">
            <div className="relative rounded-2xl border-2 border-dashed border-[#2E9E5B] bg-[#FAFAF7] p-4 shadow-inner mb-4">
              {/* eslint-disable-next-line @next/next/no-img-element */}
              <img
                src="/vitalsense_download_qr.png"
                alt="Scan to Download VitalSense"
                className="h-60 w-60 rounded-xl object-contain"
              />
              <span className="absolute -bottom-3 left-1/2 -translate-x-1/2 rounded-full bg-[#2E9E5B] px-3 py-0.5 text-[11px] font-extrabold text-white uppercase shadow-sm whitespace-nowrap">
                Scan with Phone Camera
              </span>
            </div>

            <p className="text-xs text-[#1C1C1C]/70 mt-3 mb-4">
              Open your phone camera to download from <strong>github.com/alexansh/vitalsense</strong>
            </p>

            <a
              href="https://github.com/alexansh/VitalSense/releases/download/version/app-debug.apk"
              className="flex items-center justify-center gap-2 w-full py-3 rounded-xl bg-[#2E9E5B] text-white text-xs font-extrabold hover:bg-[#25834b] transition"
            >
              <Download className="h-4 w-4" /> Download APK Directly (67 MB)
            </a>
          </div>
        )}
      </div>
    </main>
  )
}
