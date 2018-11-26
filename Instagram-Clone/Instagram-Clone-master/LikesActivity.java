package merousha.com.instagramclone2.Likes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.media.FaceDetector;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import merousha.com.instagramclone2.R;

/**
 * Created by User on 5/28/2017.
 */

public class LikesActivity extends Activity {
    private static final int TAKE_PICTURE_CODE =2;
    private static final int MAX_FACES = 5;

    private Bitmap cameraBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_camera);

        ((Button)findViewById(R.id.take_picture)).setOnClickListener(btnClick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(TAKE_PICTURE_CODE == requestCode){
            processCameraImage(data);
        }
    }

    private void openCamera(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, TAKE_PICTURE_CODE);
    }

    private void processCameraImage(Intent intent){
        setContentView(R.layout.detectlayout);

        ((Button)findViewById(R.id.detect_face)).setOnClickListener(btnClick);

        ImageView imageView = (ImageView)findViewById(R.id.image_view);

        cameraBitmap = (Bitmap)intent.getExtras().get("data");

        imageView.setImageBitmap(cameraBitmap);
    }

    private void detectFaces(){
        if(null != cameraBitmap){
            int width = cameraBitmap.getWidth();
            int height = cameraBitmap.getHeight();

            FaceDetector detector = new FaceDetector(width, height,LikesActivity.MAX_FACES);
            FaceDetector.Face[] faces = new FaceDetector.Face[LikesActivity.MAX_FACES];

            Bitmap bitmap565 = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Paint ditherPaint = new Paint();
            Paint drawPaint = new Paint();

            ditherPaint.setDither(true);
            drawPaint.setColor(Color.RED);
            drawPaint.setStyle(Paint.Style.STROKE);
            drawPaint.setStrokeWidth(2);

            Canvas canvas = new Canvas();
            canvas.setBitmap(bitmap565);
            canvas.drawBitmap(cameraBitmap, 0, 0, ditherPaint);

            int facesFound = detector.findFaces(bitmap565, faces);
            PointF midPoint = new PointF();
            float eyeDistance = 0.0f;
            float confidence = 0.0f;

            Log.i("FaceDetector", "Number of faces found: " + facesFound);

            if(facesFound > 0)
            {
                for(int index=0; index<facesFound; ++index){
                    faces[index].getMidPoint(midPoint);
                    eyeDistance = faces[index].eyesDistance();
                    confidence = faces[index].confidence();

                    Log.i("FaceDetector",
                            "Confidence: " + confidence +
                                    ", Eye distance: " + eyeDistance +
                                    ", Mid Point: (" + midPoint.x + ", " + midPoint.y + ")");

                    canvas.drawRect((int)midPoint.x - eyeDistance ,
                            (int)midPoint.y - eyeDistance ,
                            (int)midPoint.x + eyeDistance,
                            (int)midPoint.y + eyeDistance, drawPaint);
                }
            }

            String filepath = Environment.getExternalStorageDirectory() + "/facedetect" + System.currentTimeMillis() + ".jpg";

            try {
                FileOutputStream fos = new FileOutputStream(filepath);

                bitmap565.compress(Bitmap.CompressFormat.JPEG, 90, fos);

                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ImageView imageView = (ImageView)findViewById(R.id.image_view);

            imageView.setImageBitmap(bitmap565);
        }
    }

    private View.OnClickListener btnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.take_picture: openCamera(); break;
                case R.id.detect_face: detectFaces(); break;
            }
        }
    };
}
    /**
     * BottomNavigationView setup
     */
  //  private void setupBottomNavigationView(){
  //      Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
  //      BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
  //      BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
  //      BottomNavigationViewHelper.enableNavigation(mContext, this,bottomNavigationViewEx);
  //      Menu menu = bottomNavigationViewEx.getMenu();
  //      MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
  //      menuItem.setChecked(true);
  //  }
//}
