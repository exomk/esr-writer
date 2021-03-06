/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (C) 2018  EXO Service Solutions
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301  USA
 *
 * You can contact us at contact4exo@exo.mk
 */

package com.exo.esr;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Arrays;

public class ESRWriterService extends HostApduService {

    private static final String TAG = "CardService";

    // AID for registrator card service.
    private static final String CARD_AID = "FA000000000000000000000A";

    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String APDU_HEADER = "00A40400";

    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = HexStringToByteArray("9000");

    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
    private static final byte[] UNKNOWN_CMD_SW = HexStringToByteArray("0000");

    private static final byte[] SELECT_APDU = BuildSelectApdu(CARD_AID);

    private static final byte[] STATUS_OK_APDU = HexStringToByteArray("00D00000");

    private Context context;

    private static ERegisterType selectedRegisterType = ERegisterType.REGISTER_IN;

    public ESRWriterService() {
        this.context = this;
    }

    /**
     * Utility method to convert a byte array to a hexadecimal string.
     *
     * @param bytes Bytes to convert
     * @return String, containing hexadecimal representation.
     */
    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;

        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }

        return new String(hexChars);
    }

    /**
     * Utility method to convert a hexadecimal string to a byte string.
     * <p>
     * <p>Behavior with input strings containing non-hexadecimal characters is undefined.
     *
     * @param s String containing hexadecimal characters to convert
     * @return Byte array generated from input
     * @throws java.lang.IllegalArgumentException if input length is incorrect
     */
    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len;
        byte[] data; // Allocate 1 byte per 2 hex characters

        len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }

        data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }

        return data;
    }

    /**
     * Utility method to concatenate two byte arrays.
     *
     * @param first First array
     * @param rest  Any remaining arrays
     * @return Concatenated copy of input arrays
     */
    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength;
        byte[] result;
        int offset;

        totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }

        result = Arrays.copyOf(first, totalLength);
        offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }

        return result;
    }

    /**
     * Build APDU for SELECT AID command. This command indicates which service a reader is
     * interested in communicating with. See ISO 7816-4.
     *
     * @param aid Application ID (AID) to select
     * @return APDU for SELECT AID command
     */
    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(APDU_HEADER + String.format("%02X", aid.length() / 2) + aid);
    }

    public static void setRegisterType(ERegisterType registerType) {
        selectedRegisterType = registerType;
    }

    @Override
    public void onCreate() {
    }

    /**
     * Called if the connection to the NFC card is lost, in order to let the application know the
     * cause for the disconnection (either a lost link, or another AID being selected by the
     * reader).
     *
     * @param reason Either DEACTIVATION_LINK_LOSS or DEACTIVATION_DESELECTED
     */
    @Override
    public void onDeactivated(int reason) {

    }

    /**
     * This method will be called when a command APDU has been received from a remote device. A
     * response APDU can be provided directly by returning a byte-array in this method. In general
     * response APDUs must be sent as quickly as possible, given the fact that the user is likely
     * holding his device over an NFC reader when this method is called.
     * <p>
     * <p class="note">If there are multiple services that have registered for the same AIDs in
     * their meta-data entry, you will only get called if the user has explicitly selected your
     * service, either as a default or just for the next tap.
     * <p>
     * <p class="note">This method is running on the main thread of your application. If you
     * cannot return a response APDU immediately, return null and use the {@link
     * #sendResponseApdu(byte[])} method later.
     *
     * @param commandApdu The APDU that received from the remote device
     * @param extras      A bundle containing extra data. May be null.
     * @return a byte-array containing the response APDU, or null if no response APDU can be sent
     * at this point.
     */
    // BEGIN_INCLUDE(processCommandApdu)
    @Override
    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.i(TAG, "Received APDU: " + ByteArrayToHexString(commandApdu));

        /* If the APDU matches the SELECT AID command for this service, send the card id, followed by a SELECT_OK status trailer (0x9000). */
        if (Arrays.equals(SELECT_APDU, commandApdu)) {
            String cardId;
            String checkType = selectedRegisterType.registerType();
            SharedPreferences sharedPreferences;

            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            cardId = sharedPreferences.getString(ESRSetupActivity.CARD_ID, "");

            Log.i(TAG, "Sending ESR id: " + cardId);

            return ConcatArrays(HexStringToByteArray(cardId), HexStringToByteArray(checkType), SELECT_OK_SW);
        } else {
            if (Arrays.equals(STATUS_OK_APDU, commandApdu)) {

                new Thread(new Runnable() {
                    public void run() {
                        Intent intent;
                        final ToneGenerator toneGenerator = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);

                        // Stop current activity
                        sendBroadcast(new Intent("registration_success"));

                        intent = new Intent(context, ESRCardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        toneGenerator.startTone(ToneGenerator.TONE_PROP_BEEP);
                    }
                }).start();

                return null;
            } else {
                return UNKNOWN_CMD_SW;
            }
        }
    }
}
