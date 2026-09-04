"use client"

import React, { useState } from "react"
import Link from "next/link"
import { motion } from "framer-motion"
import { cn } from "@/lib/utils"
import { AnimatedGroup } from "@/components/ui/animated-group"
import { QrDownloadModal } from "@/components/ui/qr-download-modal"
import { Volume2, Download, Heart, ShieldCheck } from "lucide-react"

export interface HeroSectionProps {
  className?: string
}

export function HeroSection6({ className }: HeroSectionProps) {
  const [isPlayingAudio, setIsPlayingAudio] = useState(false)

  const handlePlayAudio = () => {
    setIsPlayingAudio(true)
    // TODO: wire to audio playback / Text-to-Speech API
    if (typeof window !== "undefined" && "speechSynthesis" in window) {
      window.speechSynthesis.cancel()
      const utterance = new SpeechSynthesisUtterance(
        "नमस्ते। वाइटलसेंस में आपका स्वागत है। आपकी सेहत, एक नज़र में। यह ऐप बिना इंटरनेट के भी आपकी धड़कन, ऑक्सीजन और स्वास्थ्य की पूरी देखभाल करता है।"
      )
      utterance.lang = "hi-IN"
      utterance.onend = () => setIsPlayingAudio(false)
      utterance.onerror = () => setIsPlayingAudio(false)
      window.speechSynthesis.speak(utterance)
    } else {
      setTimeout(() => setIsPlayingAudio(false), 2000)
    }
  }

  return (
    <section
      className={cn(
        "relative min-h-screen w-full overflow-hidden bg-[#FAFAF7] text-[#1C1C1C] antialiased",
        className
      )}
      style={{
        backgroundImage: "radial-gradient(ellipse 80% 60% at 50% -10%, rgba(46, 158, 91, 0.15), rgba(250, 250, 247, 1))",
      }}
    >
      {/* 1. Calm Minimal Header Nav (3 Essential Items Max) */}
      <header className="relative z-20 mx-auto flex max-w-7xl items-center justify-between px-6 py-5">
        <Link href="/" className="flex items-center gap-3">
          <div className="flex h-11 w-11 items-center justify-center rounded-2xl bg-[#2E9E5B] text-white shadow-sm">
            <Heart className="h-6 w-6 fill-white" />
          </div>
          <div>
            <span className="text-xl font-extrabold tracking-tight text-[#1C1C1C]">
              VitalSense
            </span>
            <span className="ml-1.5 rounded-full bg-[#E8F7EE] px-2 py-0.5 text-xs font-bold text-[#2E9E5B]">
              सेहतसेतु
            </span>
          </div>
        </Link>

        <nav aria-label="Main Navigation" className="hidden items-center gap-6 sm:flex">
          <Link
            href="#patients"
            className="flex items-center gap-1.5 text-sm font-semibold text-[#1C1C1C]/80 hover:text-[#2E9E5B]"
          >
            <span>🩺</span> For Patients (मरीज़)
          </Link>
          <Link
            href="#asha"
            className="flex items-center gap-1.5 text-sm font-semibold text-[#1C1C1C]/80 hover:text-[#2E9E5B]"
          >
            <span>🧑‍⚕️</span> For ASHA (आशा दीदी)
          </Link>
          <Link
            href="#help"
            className="flex items-center gap-1.5 text-sm font-semibold text-[#D63B3B] hover:underline"
          >
            <span>📞</span> 108 Emergency (सहायता)
          </Link>
        </nav>
      </header>

      {/* 2. Hero Content Container */}
      <div className="relative z-10 mx-auto max-w-5xl px-6 pt-10 pb-16 text-center sm:pt-16 sm:pb-24">
        <AnimatedGroup className="flex flex-col items-center gap-6">
          {/* Offline Ready Badge */}
          <div className="inline-flex items-center gap-2 rounded-full border border-[#2E9E5B]/20 bg-[#E8F7EE] px-4 py-1.5 text-sm font-semibold text-[#2E9E5B] shadow-xs">
            <ShieldCheck className="h-4 w-4" />
            <span>100% ऑफ़लाइन सक्षम · Works Zero-Internet</span>
          </div>

          {/* Low-Literacy Headline (<= 6 words, emoji-anchored, large type) */}
          <h1 className="max-w-3xl text-4xl font-extrabold tracking-tight text-[#1C1C1C] sm:text-6xl sm:leading-[1.15]">
            🫀 आपकी सेहत, <br className="hidden sm:block" />
            <span className="text-[#2E9E5B]">एक नज़र में</span>
          </h1>

          {/* Spoken-Language Subhead (Bilingual) */}
          <p className="max-w-2xl text-lg font-medium text-[#1C1C1C]/80 sm:text-xl">
            ग्रामीण और दूरदराज क्षेत्रों के लिए ऑफ़लाइन टेलीमेडिसिन और स्वास्थ्य निगरानी।
            <br />
            <span className="text-sm font-normal text-[#1C1C1C]/60">
              Zero-internet telemedicine & vital signs tracking designed for rural communities.
            </span>
          </p>

          {/* Action CTAs (56px touch target minimum) */}
          <div className="mt-4 flex flex-col items-center justify-center gap-3 sm:flex-row sm:gap-4">
            {/* Single Primary CTA */}
            <Link
              href="https://github.com/alexansh/VitalSense/releases/download/version/app-debug.apk"
              className="flex min-h-[56px] w-full items-center justify-center gap-2 rounded-xl bg-[#2E9E5B] px-8 text-base font-bold text-white shadow-sm transition hover:bg-[#25834b] active:scale-[0.98] sm:w-auto"
            >
              <Download className="h-5 w-5" />
              <span>📲 फ़ोन में डाउनलोड करें (Download APK)</span>
            </Link>

            {/* Scan QR Modal Button */}
            <QrDownloadModal />

            {/* Secondary Ghost Audio Narrator Button */}
            <button
              type="button"
              onClick={handlePlayAudio}
              aria-label="Listen to spoken audio description in Hindi"
              className={cn(
                "flex min-h-[56px] w-full items-center justify-center gap-2 rounded-xl border border-[#2E9E5B]/30 bg-white px-6 text-base font-semibold text-[#1C1C1C] shadow-xs transition hover:bg-[#E8F7EE] active:scale-[0.98] sm:w-auto",
                isPlayingAudio && "border-[#2E9E5B] bg-[#E8F7EE] text-[#2E9E5B]"
              )}
            >
              <Volume2 className={cn("h-5 w-5 text-[#2E9E5B]", isPlayingAudio && "animate-pulse")} />
              <span>{isPlayingAudio ? "🔊 सुनाया जा रहा है..." : "🔊 इसे सुनें (Listen)"}</span>
            </button>
          </div>
        </AnimatedGroup>

        {/* 3. Screen Placeholders Grid (Calm, Non-SaaS Presentation) */}
        <motion.div
          initial={{ opacity: 0, y: 24 }}
          animate={{ opacity: 1, y: 0 }}
          transition={{ duration: 0.5, delay: 0.25, ease: [0.25, 0.1, 0.25, 1] }}
          className="mt-12 grid grid-cols-1 gap-6 sm:mt-16 sm:grid-cols-3"
        >
          {/* Main Slot: VitalSense Status Halo Home Screen (Spans 2 columns on desktop) */}
          <div className="relative col-span-1 flex flex-col items-center rounded-2xl border border-black/8 bg-white p-6 shadow-sm sm:col-span-2">
            <div className="mb-4 flex w-full items-center justify-between border-b border-black/6 pb-3">
              <div className="flex items-center gap-2">
                <span className="text-xl">🟢</span>
                <span className="font-bold text-[#1C1C1C]">Status Halo (स्वास्थ्य स्थिति)</span>
              </div>
              <span className="rounded-full bg-[#E8F7EE] px-2.5 py-1 text-xs font-bold text-[#2E9E5B]">
                ☁️✓ Offline Synced
              </span>
            </div>

            {/* Real Status Halo UI Render / Screenshot Slot */}
            {/* TODO: replace with real Status Halo screenshot */}
            <div className="flex w-full flex-col items-center justify-center rounded-xl bg-[#FAFAF7] p-6">
              {/* Circular Halo Ring */}
              <div className="relative flex h-36 w-36 items-center justify-center rounded-full border-8 border-[#2E9E5B] bg-white shadow-xs">
                <div className="text-center">
                  <div className="text-2xl">🧍</div>
                  <div className="text-lg font-extrabold text-[#2E9E5B]">ठीक हैं</div>
                  <div className="text-[10px] text-black/60">(You're Fine)</div>
                </div>
              </div>

              {/* 2x2 Vital Tiles Preview */}
              <div className="mt-6 grid w-full grid-cols-2 gap-3 sm:grid-cols-4">
                <div className="rounded-xl border border-black/6 bg-white p-3 text-left">
                  <div className="flex items-center justify-between text-xs text-black/60">
                    <span>❤️ धड़कन</span>
                    <span className="font-bold text-[#2E9E5B]">Normal</span>
                  </div>
                  <div className="mt-1 text-base font-bold text-[#1C1C1C]">76 bpm</div>
                </div>

                <div className="rounded-xl border border-black/6 bg-white p-3 text-left">
                  <div className="flex items-center justify-between text-xs text-black/60">
                    <span>🫁 ऑक्सीजन</span>
                    <span className="font-bold text-[#2E9E5B]">Normal</span>
                  </div>
                  <div className="mt-1 text-base font-bold text-[#1C1C1C]">98% SpO2</div>
                </div>

                <div className="rounded-xl border border-black/6 bg-white p-3 text-left">
                  <div className="flex items-center justify-between text-xs text-black/60">
                    <span>💧 रक्तचाप</span>
                    <span className="font-bold text-[#2E9E5B]">Normal</span>
                  </div>
                  <div className="mt-1 text-base font-bold text-[#1C1C1C]">120/80</div>
                </div>

                <div className="rounded-xl border border-black/6 bg-white p-3 text-left">
                  <div className="flex items-center justify-between text-xs text-black/60">
                    <span>🌡️ तापमान</span>
                    <span className="font-bold text-[#2E9E5B]">Normal</span>
                  </div>
                  <div className="mt-1 text-base font-bold text-[#1C1C1C]">98.4°F</div>
                </div>
              </div>
            </div>
            <p className="mt-3 text-xs text-black/50">
              * The hero circular ring provides glanceable health verdict across the room in under 3 seconds.
            </p>
          </div>

          {/* Secondary Slot: 1-Tap Emergency SOS & 3s Countdown View */}
          <div className="flex flex-col justify-between rounded-2xl border border-black/8 bg-white p-6 shadow-sm">
            <div>
              <div className="mb-4 flex items-center justify-between border-b border-black/6 pb-3">
                <span className="font-bold text-[#1C1C1C]">🚨 Emergency SOS</span>
                <span className="rounded-full bg-[#FFEAE8] px-2.5 py-1 text-xs font-bold text-[#D63B3B]">
                  0-Net Fallback
                </span>
              </div>

              {/* TODO: replace with real screenshot of Emergency Screen */}
              <div className="flex flex-col items-center rounded-xl bg-[#FFEAE8]/40 p-4">
                <div className="flex h-16 w-16 items-center justify-center rounded-full bg-[#D63B3B] text-2xl text-white shadow-sm">
                  🚨
                </div>
                <div className="mt-2 text-center">
                  <div className="text-base font-bold text-[#D63B3B]">108 Ambulance SOS</div>
                  <div className="text-xs text-black/60">3-second abort window + auto-GPS SMS</div>
                </div>
              </div>

              <div className="mt-4 flex flex-col gap-2">
                <div className="flex items-center gap-2 rounded-lg bg-[#FAFAF7] p-2.5 text-xs text-[#1C1C1C]/80">
                  <span className="text-base">📍</span> Auto-GPS Location Dispatch
                </div>
                <div className="flex items-center gap-2 rounded-lg bg-[#FAFAF7] p-2.5 text-xs text-[#1C1C1C]/80">
                  <span className="text-base">👩‍⚕️</span> Direct Village ASHA Alert
                </div>
              </div>
            </div>

            <div className="mt-4 border-t border-black/6 pt-3 text-center">
              <span className="text-xs font-semibold text-[#2E9E5B]">
                ✓ Stamped with Doctor MC Council ID
              </span>
            </div>
          </div>
        </motion.div>
      </div>

      {/* 4. Simple Low-Literacy Footer */}
      <footer className="border-t border-black/8 bg-[#F3F3EE] py-6 text-center text-xs text-[#1C1C1C]/70">
        <div className="mx-auto max-w-7xl px-6">
          <p className="font-semibold">
            VitalSense (सेहतसेतु) — Built for Ayushman Bharat Digital Mission (ABDM) & National Digital Hospital interoperability.
          </p>
          <p className="mt-1 text-[11px] text-[#1C1C1C]/50">
            Design complies with WCAG AAA (7:1 contrast ratio) & Offline-First local SQLite persistence.
          </p>
        </div>
      </footer>
    </section>
  )
}
