/*
 * Copyright (c) 2010, Apigee Corporation. All rights reserved.
 * Apigee(TM) and the Apigee logo are trademarks or
 * registered trademarks of Apigee Corp. or its subsidiaries. All other
 * trademarks are the property of their respective owners.
 */
package com.apigee.sdk.oauth.impl.util.http;

import org.apache.commons.codec.net.URLCodec;
import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/*
This class contains encoding-related utility methods.
The implementation for the encodeQuery() method is borrowed from the EncodeUtil class of Apache HttpClient 3.0.1,
since the version we are currently using (4.x) does not have this functionality.
*/

public class EncodeUtil {

    private static final Logger LOG = Logger.getLogger(EncodeUtil.class);
    public static final BitSet allowed_query = new BitSet(256); // Static initializer for allowed_query
    protected static final BitSet uric = new BitSet(256); // Static initializer for uric
    protected static final BitSet reserved = new BitSet(256); // Static initializer for reserved
    protected static final BitSet alpha = new BitSet(256); // Static initializer for alpha
    protected static final BitSet digit = new BitSet(256); // Static initializer for digit
    protected static final BitSet alphanum = new BitSet(256); // Static initializer for alphanum
    protected static final BitSet mark = new BitSet(256); // Static initializer for mark
    protected static final BitSet percent = new BitSet(256); // Static initializer for percent
    protected static final BitSet hex = new BitSet(256); // Static initializer for hex
    protected static final BitSet escaped = new BitSet(256); // Static initializer for escaped
    protected static final BitSet unreserved = new BitSet(256);// Static initializer for unreserved

    /**
     * Escape and encode a string regarded as the query component of an URI with
     * a given charset.
     * When a query string is not misunderstood the reserved special characters
     * ("&amp;", "=", "+", ",", and "$") within a query component, this method
     * is recommended to use in encoding the whole query.
     */

    public static String encodeQuery(String unescaped, String charset) throws Exception {
        return encode(unescaped, allowed_query, charset);
    }

    /**
     * Escape and encode a given string with allowed characters not to be
     * escaped and a given charset.
     *
     * @param unescaped a string
     * @param allowed   allowed characters not to be escaped
     * @param charset   the charset
     * @return the escaped string
     */

    private static String encode(String unescaped, BitSet allowed, String charset) throws Exception {
        byte[] rawdata = URLCodec.encodeUrl(allowed, getBytes(unescaped, charset));
        return getAsciiString(rawdata);
    }

    /**
     * Converts the specified string to a byte array.  If the charset is not supported the
     * default system charset is used.
     *
     * @param data    the string to be encoded
     * @param charset the desired character encoding
     * @return The resulting byte array.
     */

    private static byte[] getBytes(final String data, String charset) {

        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }

        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }

        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            LOG.error(String.format("Unsupported encoding: " + charset + ". System encoding used."));
            return data.getBytes();
        }
    }

    /**
     * Converts the byte array of ASCII characters to a string. This method is
     * to be used when decoding content of HTTP elements (such as response
     * headers)
     *
     * @param data the byte array to be encoded
     * @return The string representation of the byte array
     */

    private static String getAsciiString(final byte[] data) throws UnsupportedEncodingException {
        return getAsciiString(data, 0, data.length);
    }

    /**
     * Converts the byte array of ASCII characters to a string. This method is
     * to be used when decoding content of HTTP elements (such as response
     * headers)
     *
     * @param data   the byte array to be encoded
     * @param offset the index of the first byte to encode
     * @param length the number of bytes to encode
     * @return The string representation of the byte array
     */
    private static String getAsciiString(final byte[] data, int offset, int length) throws UnsupportedEncodingException {

        if (data == null) {
            throw new IllegalArgumentException("Parameter may not be null");
        }
        return new String(data, offset, length, "US-ASCII");
    }

    /**
     * BitSet for reserved.
     * <p><blockquote><pre>
     * reserved      = ";" | "/" | "?" | ":" | "@" | "&amp;" | "=" | "+" |
     *                 "$" | ","
     * </pre></blockquote><p>
     */

    static {
        reserved.set(';');
        reserved.set('/');
        reserved.set('?');
        reserved.set(':');
        reserved.set('@');
        reserved.set('&');
        reserved.set('=');
        reserved.set('+');
        reserved.set('$');
        reserved.set(',');
    }

    /**
     * BitSet for alpha.
     * <p><blockquote><pre>
     * alpha         = lowalpha | upalpha
     * </pre></blockquote><p>
     */

    static {
        for (int i = 'a'; i <= 'z'; i++) {
            alpha.set(i);
        }
        for (int i = 'A'; i <= 'Z'; i++) {
            alpha.set(i);
        }
    }

    /**
     * BitSet for digit.
     * <p><blockquote><pre>
     * digit    = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" |
     *            "8" | "9"
     * </pre></blockquote><p>
     */

    static {
        for (int i = '0'; i <= '9'; i++) {
            digit.set(i);
        }
    }

    /**
     * BitSet for alphanum (join of alpha &amp; digit).
     * <p><blockquote><pre>
     *  alphanum      = alpha | digit
     * </pre></blockquote><p>
     */

    static {
        alphanum.or(alpha);
        alphanum.or(digit);
    }

    /**
     * BitSet for mark.
     * <p><blockquote><pre>
     * mark          = "-" | "_" | "." | "!" | "~" | "*" | "'" |
     *                 "(" | ")"
     * </pre></blockquote><p>
     */

    static {
        mark.set('-');
        mark.set('_');
        mark.set('.');
        mark.set('!');
        mark.set('~');
        mark.set('*');
        mark.set('\'');
        mark.set('(');
        mark.set(')');
    }

    /**
     * The percent "%" character always has the reserved purpose of being the
     * escape indicator, it must be escaped as "%25" in order to be used as
     * data within a URI.
     */

    static {
        percent.set('%');
    }

    /**
     * BitSet for hex.
     * <p><blockquote><pre>
     * hex           = digit | "A" | "B" | "C" | "D" | "E" | "F" |
     *                         "a" | "b" | "c" | "d" | "e" | "f"
     * </pre></blockquote><p>
     */

    static {
        hex.or(digit);
        for (int i = 'a'; i <= 'f'; i++) {
            hex.set(i);
        }
        for (int i = 'A'; i <= 'F'; i++) {
            hex.set(i);
        }
    }

    /**
     * Data characters that are allowed in a URI but do not have a reserved
     * purpose are called unreserved.
     * <p><blockquote><pre>
     * unreserved    = alphanum | mark
     * </pre></blockquote><p>
     */

    static {
        unreserved.or(alphanum);
        unreserved.or(mark);
    }

    /**
     * BitSet for escaped.
     * <p><blockquote><pre>
     * escaped       = "%" hex hex
     * </pre></blockquote><p>
     */

    static {
        escaped.or(percent);
        escaped.or(hex);
    }

    /**
     * BitSet for uric.
     * <p><blockquote><pre>
     * uric          = reserved | unreserved | escaped
     * </pre></blockquote><p>
     */

    static {
        uric.or(reserved);
        uric.or(unreserved);
        uric.or(escaped);
    }

    /**
     * Those characters that are allowed for the query component.
     */

    static {
        allowed_query.or(uric);
        allowed_query.clear('%');
    }

}

