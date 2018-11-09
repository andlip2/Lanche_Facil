package lanchefacil.dalksoft.com.helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;

import com.takusemba.spotlight.shape.Shape;

public class RoundRectangle implements Shape {

    private float width;
    private float height;
    private float left;
    private float top;

    public RoundRectangle(float left, float top, float width, float height) {
        this.left = left;
        this.top = top;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(Canvas canvas, PointF point, float value, Paint paint) {

        //exemplo de usabilidade
        //new RoundRectangle(btCadastrarAluno.getLeft(), btCadastrarAluno.getTop() * 2, btCadastrarAluno.getWidth(), btCadastrarAluno.getHeight())

        Rect r = new Rect((int) left, (int) top , (int) ((int) left+width) , (int) ((int) top +height));

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(r, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        canvas.drawRect(r, paint);

    }

    @Override
    public int getHeight() {
        return (int) height;
    }

    @Override
    public int getWidth() {
        return (int) width;
    }

}
