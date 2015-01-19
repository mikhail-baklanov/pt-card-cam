package ru.relex.hakaton;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamImageTransformer;
import com.github.sarxos.webcam.WebcamPanel;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.gtranslate.Audio;
import com.gtranslate.Language;

public class ScanCodeAndSend extends JFrame implements Runnable, ThreadFactory {

  private static final String REST_URL         = "http://127.0.0.1:8080/pt-api-0.0.5-SNAPSHOT/rest";

  private static final long   serialVersionUID = 6441489157408381878L;

  private Executor            executor         = Executors.newSingleThreadExecutor(this);

  private Webcam              webcam           = null;
  private WebcamPanel         panel            = null;
  private JTextArea           textarea         = null;

  public static BufferedImage getFlippedImage(BufferedImage bi) {

    BufferedImage flipped = new BufferedImage(bi.getWidth(), bi.getHeight(),
        BufferedImage.TYPE_3BYTE_BGR);
    AffineTransform tran = AffineTransform.getTranslateInstance(bi.getWidth(), 0);
    AffineTransform flip = AffineTransform.getScaleInstance(-1d, 1d);
    tran.concatenate(flip);

    Graphics2D g = flipped.createGraphics();
    g.setTransform(tran);
    g.drawImage(bi, 0, 0, null);
    g.dispose();

    return flipped;
  }

  private BufferedImage scale(BufferedImage image) {

    BufferedImage scaled = new BufferedImage(image.getWidth(), image.getHeight(),
        BufferedImage.TYPE_3BYTE_BGR);
    AffineTransform scale = AffineTransform.getScaleInstance(2d, 2d);

    Graphics2D g = scaled.createGraphics();
    g.setTransform(scale);
    g.drawImage(image, 0, 0, null);
    g.dispose();
    return scaled;
  }

  public ScanCodeAndSend() {
    super();

    setLayout(new FlowLayout());
    setTitle("Read QR / Bar Code With Webcam");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Dimension size = WebcamResolution.QVGA.getSize();

    webcam = Webcam.getWebcams().get(0);
    webcam.setViewSize(size);
    webcam.setImageTransformer(new WebcamImageTransformer() {

      @Override
      public BufferedImage transform(BufferedImage arg0) {
        // return getFlippedImage(arg0);
        // return scale(arg0);
        return arg0;
      }
    });

    panel = new WebcamPanel(webcam);
    panel.setPreferredSize(size);

    textarea = new JTextArea();
    textarea.setEditable(false);
    textarea.setPreferredSize(size);

    add(panel);
    add(textarea);

    pack();
    setVisible(true);

    executor.execute(this);
  }

  @Override
  public void run() {

    SenderFacade sf = new SenderFacade(REST_URL + "/passway/entrance", "");
    do {
      try {
        Thread.sleep(100);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }

      Result result = null;
      BufferedImage image = null;

      if (webcam.isOpen()) {

        if ((image = webcam.getImage()) == null) {
          continue;
        }

        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        try {
          result = new MultiFormatReader().decode(bitmap);
        }
        catch (NotFoundException e) {
          // fall thru, it means there is no QR code in image
        }
      }

      if (result != null && !result.getText().equals(textarea.getText())) {
        List<String> voiceMessages = new ArrayList<String>();
        java.awt.Toolkit.getDefaultToolkit().beep();
        String id = result.getText();
        PassInfo userInfo = sf.sendId(id);
        String textMessage;
        if (userInfo != null) {
          textMessage = "Идентификатор " + id + " обнаружен\nПользователь: " + userInfo;
          voiceMessages.add(HelloMessage.getHiMessage(userInfo.getFirstName(),
              userInfo.getMiddleName(), userInfo.getLastName()));
          if (userInfo.getUserMessages().size() > 0) {
            voiceMessages.add("Для вас есть сообщения");
            int i=1;
            for (UserMessage us: userInfo.getUserMessages()) {
              voiceMessages.add("Сообщение "+ (i++));
              voiceMessages.add(us.getText());
            }
          }
        }
        else {
          textMessage = "Идентификатор " + id + " не обнаружен";
          voiceMessages.add(HelloMessage.getBrrrMessage());
        }

        textarea.setText(textMessage);

        try {
          Audio audio = Audio.getInstance();
          for (String s: voiceMessages) {
          InputStream sound = audio.getAudio(s, Language.RUSSIAN);
          audio.play(sound);
          Thread.sleep(300);
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
      }

    }
    while (true);
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = new Thread(r, "example-runner");
    t.setDaemon(true);
    return t;
  }

  public static void main(String[] args) {
    new ScanCodeAndSend();
  }
}
