package com.galkonltd.qwikpik.jna;

import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinGDI;
import com.sun.jna.platform.win32.WinNT;

import java.awt.*;
import java.awt.image.*;

public final class JNAScreenShot {

    public static BufferedImage getScreenshot(Rectangle bounds) {
        System.out.println(bounds);
        WinDef.HDC windowDC = GDI.GetDC(USER.GetDesktopWindow());
        WinDef.HBITMAP outputBitmap = GDI.CreateCompatibleBitmap(windowDC, bounds.width, bounds.height);
        try {
            WinDef.HDC blitDC = GDI.CreateCompatibleDC(windowDC);
            try {
                WinNT.HANDLE oldBitmap = GDI.SelectObject(blitDC, outputBitmap);
                try {
                    GDI.BitBlt(blitDC, 0, 0, bounds.width, bounds.height, windowDC, bounds.x, bounds.y, GDI32.SRCCOPY);
                } finally {
                    GDI.SelectObject(blitDC, oldBitmap);
                }
                WinGDI.BITMAPINFO bi = new WinGDI.BITMAPINFO(40);
                bi.bmiHeader.biSize = 40;
                boolean ok = GDI.GetDIBits(blitDC, outputBitmap, 0, bounds.height, (byte[]) null, bi, WinGDI.DIB_RGB_COLORS);
                if (ok) {
                    WinGDI.BITMAPINFOHEADER bih = bi.bmiHeader;
                    bih.biHeight = -Math.abs(bih.biHeight);
                    bi.bmiHeader.biCompression = 0;
                    BufferedImage image = bufferedImageFromBitmap(blitDC, outputBitmap, bi);
                    return image;
                } else {
                    return null;
                }
            } finally {
                GDI.DeleteObject(blitDC);
            }
        } finally {
            GDI.DeleteObject(outputBitmap);
        }
    }

    private static BufferedImage bufferedImageFromBitmap(WinDef.HDC blitDC, WinDef.HBITMAP outputBitmap, WinGDI.BITMAPINFO bi) {
        WinGDI.BITMAPINFOHEADER bih = bi.bmiHeader;
        int height = Math.abs(bih.biHeight);
        ColorModel cm;
        DataBuffer buffer;
        WritableRaster raster;
        int strideBits = (bih.biWidth * bih.biBitCount);
        int strideBytesAligned = (((strideBits - 1) | 0x1F) + 1) >> 3;
        final int strideElementsAligned;
        switch (bih.biBitCount) {
            case 16:
                strideElementsAligned = strideBytesAligned / 2;
                cm = new DirectColorModel(16, 0x7C00, 0x3E0, 0x1F);
                buffer = new DataBufferUShort(strideElementsAligned * height);
                raster = Raster.createPackedRaster(buffer, bih.biWidth, height, strideElementsAligned, ((DirectColorModel) cm).getMasks(), null);
                break;
            case 32:
                strideElementsAligned = strideBytesAligned / 4;
                cm = new DirectColorModel(32, 0xFF0000, 0xFF00, 0xFF);
                buffer = new DataBufferInt(strideElementsAligned * height);
                raster = Raster.createPackedRaster(buffer, bih.biWidth, height, strideElementsAligned, ((DirectColorModel) cm).getMasks(), null);
                break;
            default:
                throw new IllegalArgumentException("Unsupported bit count: " + bih.biBitCount);
        }
        final boolean ok;
        switch (buffer.getDataType()) {
            case DataBuffer.TYPE_INT: {
                int[] pixels = ((DataBufferInt) buffer).getData();
                ok = GDI.GetDIBits(blitDC, outputBitmap, 0, raster.getHeight(), pixels, bi, 0);
            }
            break;
            case DataBuffer.TYPE_USHORT: {
                short[] pixels = ((DataBufferUShort) buffer).getData();
                ok = GDI.GetDIBits(blitDC, outputBitmap, 0, raster.getHeight(), pixels, bi, 0);
            }
            break;
            default:
                throw new AssertionError("Unexpected buffer element type: " + buffer.getDataType());
        }
        if (ok) {
            return new BufferedImage(cm, raster, false, null);
        } else {
            return null;
        }
    }

    private static final User32 USER = User32.INSTANCE;
    private static final GDI32 GDI = GDI32.INSTANCE;
}

