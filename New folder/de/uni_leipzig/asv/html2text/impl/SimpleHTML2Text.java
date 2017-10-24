/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uni_leipzig.asv.html2text.impl;

import de.uni_leipzig.asv.encodingdetector.utils.EncodingDetector;
import de.uni_leipzig.asv.html2text.textextractor.main.TextExtractor;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.htmlparser.jericho.Config;
import net.htmlparser.jericho.LoggerProvider;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Segment;
import net.htmlparser.jericho.Source;

/**
 *
 * @author ccui
 */
public class SimpleHTML2Text {

    //html encoding
    private transient String encoding = "";
    //row html content
    private transient byte[] buffer;
    //is html2text successful
    private transient boolean isSuccessful;
    private transient String htmlContent = "";
    private transient String savedurl;

    public SimpleHTML2Text(final URL url) {
        this.loadHTML(url);
    }

    public SimpleHTML2Text(final String filename) {
        this.loadHTML(filename);
    }

    public SimpleHTML2Text(final URL url, final String encoding) {
        this.loadHTML(url);
        //set default encoding
        this.encoding = encoding;
    }

    public SimpleHTML2Text(final String filename, final String encoding) {
        this.loadHTML(filename);
        //set default encoding
        this.encoding = encoding;
    }

    /**
     * download the html and save it into memory.
     */
    private void loadHTML(final Object obj) {
        DataInputStream dis = null;
        URLConnection conn;
        byte[] tmpbuffer = new byte[4096000];

        int count = 0;
        try {
            // is url, download it.
            if (obj instanceof URL) {
                final URL url = (URL) obj;
                savedurl = url.toString();
                conn = url.openConnection();
                conn.setConnectTimeout(3000);
                conn.setReadTimeout(2000);
                dis = new DataInputStream(conn.getInputStream());
            } else if (obj instanceof String) {
                dis = new DataInputStream(new FileInputStream((String) obj));
            }
            while (true) {
                tmpbuffer[count] = dis.readByte();
                count++;
            }

        } catch (EOFException ex) {
            isSuccessful = true;
            buffer = new byte[count];
            System.arraycopy(tmpbuffer, 0, buffer, 0, buffer.length);
            Logger.getLogger(SimpleHTML2Text.class.getName()).fine("Read end of File");
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, null, ex);
            isSuccessful = false;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, null, ex);
            isSuccessful = false;
        } catch (IOException ex) {
            Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, null, ex);
            isSuccessful = false;
        } finally {
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    /**
     * get the UTF-8 Text from HTML Content, already filter with HTML Parser, that means "Row Text of content in Html."
     * @return utf8 row text
     */
    public String getUTF8Text() {
        // the html not successful downloaded.
        if (isSuccessful) {

            //disable logger for net.htmlparser.jericho
            Config.LoggerProvider = LoggerProvider.DISABLED;

            String result = null;
            Source source = null;

            if (encoding.equalsIgnoreCase("")) {
                //there no default encoding
                encoding = getEncoding();
                Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, "Detected Encoding: {0}", encoding);
            } else {
                Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, "Set Default Encoding: {0}", encoding);
            }

            // convert to utf8

            final StringBuffer sBuffer = new StringBuffer();
            try {
                final InputStreamReader isr = new InputStreamReader(new ByteArrayInputStream(buffer), encoding);
                final BufferedReader bReader = new BufferedReader(isr);
                String line;
                while (bReader.ready()) {
                    line = bReader.readLine();
                    sBuffer.append(line);
                    // sometimes without this blank is html2text wrong with .?
                    sBuffer.append(" ");
                }
                htmlContent = sBuffer.toString();
                Logger.getLogger(SimpleHTML2Text.class.getName()).finest(htmlContent);
            } catch (UnsupportedEncodingException ex) {
                Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.WARNING, "URL: {0}", savedurl);
                Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.WARNING, "UnsupportedEncodingException: {0}", encoding);
            } catch (IOException ex) {
                Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.SEVERE, null, ex);
            }


            htmlContent = replaceCharset(htmlContent);

            if (htmlContent.length() > 1) {
                source = new Source(htmlContent);
                final Segment seg = new Segment(source, 0, source.length());
                final Renderer render = new Renderer(seg);
                render.setMaxLineLength(10000);
                render.setIncludeHyperlinkURLs(false);
                render.setDecorateFontStyles(false);
                result = render.toString();

                //TODO some html have no meta tag, but it seems to be, that jerichohtml can handel this situation.
                //result = source.getTextExtractor().toString();
                final TextExtractor textExtractor = new TextExtractor();

                result = textExtractor.doExtraction(result);

                if (result.length() > 1) {
                    //result = result+"\n\n\n"+encoding;
                    return result;
                }
            }
        }

        return "";
    }

    /* HTML Parser use usually META Tag for Encoding Detecting.
     * So we have to convert all of other Encoding to UTF-8 in Head Tag of Html
     * @param originalContent original html String
     * @return content, which charset is replaced with UTF-8.
     */
    private String replaceCharset(final String originalContent) {
        String content = originalContent;
        /**
         * we could check ,if the encoding in html and encoding that we detected is different or not.
         * convert to utf8
         * replace with charset:UTF-8
         * <META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=iso-8859-1">
         * <META HTTP-EQUIV="Content-Type" CONTENT="text/html;CHARSET=UTF-8">
         *
         * */
        final Source source = new Source(content);
        final String oldcharset = source.getEncodingSpecificationInfo();
        Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, "encoding content in html: {0}", oldcharset);
        Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, "encoding in html: {0}", source.getEncoding());
        if (oldcharset.indexOf("No encoding specified") == -1
                && !oldcharset.endsWith("mandatory XML encoding when no BOM or encoding declaration is present")) {
            final String oldencoding = source.getEncoding();
            if (oldencoding != null) {
                final String newcharset = oldcharset.replaceAll(oldencoding, "UTF-8");
                /* sometime there 2 <META HTTP-EQUIV="Content-Type"
                 * CONTENT="text/html;CHARSET=iso-8859-1"> !!!
                 * but jericho htmlpaser use the first one.
                 */
                content = content.replaceFirst(oldcharset, newcharset);
            }
            if (oldencoding == null) {
                Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, "Error while encoding detecting: {0}", savedurl);
                //System.out.println(content);
            }

            Logger.getLogger(SimpleHTML2Text.class.getName()).finest(content);
        } else {
            Logger.getLogger(SimpleHTML2Text.class.getName()).log(Level.FINE, "No Encoding Tag in HTML {1} \n Detected Encoding is {0}", new Object[]{encoding, savedurl});
        }
        return content;
    }


    /**
     * clean some errors.
     * @param content
     * @return
     */
    private String cleanText(final String content) {
         String result = content;

        /*
         * € is not in iso8859-1 standard, muss be special hander.
         */
        if (encoding.equalsIgnoreCase("iso8859-1")) {
            result = result.replace('\u0080', '€');
            result = result.replace('\u0096', '–');
            //Skoda mit iso8859-1
            result = result.replace('\u008A', 'Š');
        } else if (encoding.equalsIgnoreCase("utf-8")) {
            result = result.replace('\u0084', ' ');
            result = result.replace('\u0093', ' ');
            result = result.replace('\u0096', '-');
            result = result.replace('\uFEFF', ' ');
        }
         return result;
    }

    /**
     * guess the encoding with EncodingDetector.
     * @return
     */
    private String getEncoding() {
        final EncodingDetector encodingDetector = new EncodingDetector(buffer);
        return encodingDetector.getBestEncoding();
    }
}
