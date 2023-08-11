package cn.qbs.wa.teach.common.post.utils;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfStream;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * TODO
 *
 * @author vieux
 * @version 1.0
 * @date 2022/2/16 15:16
 */
public class PdfUtils {

    public static File pdf(List<String> imageUrlList, Integer originalHeight, Integer originalWidth, String pdfUrl) {
        float height;
        float width;
        if (originalHeight > originalWidth) {
            height = originalHeight.floatValue() * (originalWidth.floatValue() / originalHeight.floatValue());
            width = originalWidth.floatValue() * (originalWidth.floatValue() / originalHeight.floatValue());
        } else {
            height = originalHeight.floatValue() * (originalHeight.floatValue() / originalWidth.floatValue());
            width = originalWidth.floatValue() * (originalHeight.floatValue() / originalWidth.floatValue());
        }

        //new一个pdf文档
        //A4  210mm×297mm 595.0F, 842.0F
        Document doc = new Document(new Rectangle(width, height));
        try {
            //pdf写入
            PdfWriter.getInstance(doc, new FileOutputStream(pdfUrl));
            //打开文档
            doc.open();
            //遍历集合，将图片放在pdf文件
            for (int i = 0; i < imageUrlList.size(); i++) {
                //在pdf创建一页   主：此处为每一张图片是pdf文件的一页
                doc.newPage();
                //通过文件路径获取image
                Image png1 = Image.getInstance(imageUrlList.get(i));
                png1.setAbsolutePosition(0, 0);
                png1.setCompressionLevel(PdfStream.BEST_COMPRESSION);
                png1.scaleToFit(width, height);
                doc.add(png1);
            }
            doc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //输出流
        File mOutputPdfFile = new File(pdfUrl);
        if (!mOutputPdfFile.exists()) {
            mOutputPdfFile.deleteOnExit();
            return null;
        }
        //反回文件输出流
        return mOutputPdfFile;
    }



    public static int getPercent(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        if (h > w) {
            p2 = 297 / h * 100;
        } else {
            p2 = 210 / w * 100;
        }
        p = Math.round(p2);
        return p;
    }

    public static int getPercent2(float h, float w) {
        int p = 0;
        float p2 = 0.0f;
        p2 = 530 / w * 100;
        p = Math.round(p2);
        return p;
    }


}
