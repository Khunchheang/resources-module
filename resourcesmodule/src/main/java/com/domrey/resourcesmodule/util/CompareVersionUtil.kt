package com.domrey.resourcesmodule.util

import kotlin.math.max

class CompareVersionUtil(private val version: String) : Comparable<CompareVersionUtil> {

   override fun compareTo(other: CompareVersionUtil): Int {
      if (!version.matches("[0-9]+(\\.[0-9]+)*".toRegex())) return 0

      val thisParts =
         this.version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val thatParts =
         other.version.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
      val length = max(thisParts.size, thatParts.size)
      for (i in 0 until length) {
         val thisPart = if (i < thisParts.size) Integer.parseInt(thisParts[i]) else 0
         val thatPart = if (i < thatParts.size) Integer.parseInt(thatParts[i]) else 0
         if (thisPart < thatPart) return -1
         if (thisPart > thatPart) return 1
      }
      return 0
   }

   override fun equals(other: Any?): Boolean {
      if (this === other) return true
      if (other == null) return false
      return if (this.javaClass != other.javaClass) false else this.compareTo((other as CompareVersionUtil?)!!) == 0
   }

   override fun hashCode(): Int {
      return version.hashCode()
   }
}