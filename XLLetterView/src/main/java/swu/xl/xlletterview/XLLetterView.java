package swu.xl.xlletterview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class XLLetterView extends View {
    //绘制文本的画笔
    private Paint paint;
    //字母正常状态画笔的颜色
    private int text_color = Color.WHITE;
    //字母选中状态画笔颜色
    private int select_text_color = Color.MAGENTA;
    //画笔的粗细
    private int text_size = 60;

    //字母与左右的间距
    private int space_hor = 10;

    //字母之间或者字母和上下的间距
    private int space_ver = 10;

    //字母的最大高度
    private int max_letter_height = 0;

    //监听者
    private LetterChangeListener listener;

    //上一次选择字母
    private String lastLetter;

    //当前选择的字母
    private String currentLetter;

    /**
     * 构造方法：Xml代码初始化
     * @param context
     * @param attrs
     */
    public XLLetterView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null){
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.XLLetterView);

            text_color = typedArray.getColor(R.styleable.XLLetterView_text_color,text_color);
            select_text_color = typedArray.getColor(R.styleable.XLLetterView_select_text_color,select_text_color);
            text_size = typedArray.getInteger(R.styleable.XLLetterView_text_size,text_size);
            space_hor = typedArray.getInteger(R.styleable.XLLetterView_space_hor,space_hor);
            space_ver = typedArray.getInteger(R.styleable.XLLetterView_space_ver,space_ver);

            text_size = PxUtil.spToPx(text_size,context);
            space_hor = PxUtil.spToPx(space_hor,context);
            space_ver = PxUtil.spToPx(space_ver,context);

            typedArray.recycle();
        }

        //初始化
        init();
    }

    /**
     * 初始化方法
     */
    private void init(){
        //初始化画笔
        paint = new Paint();
        paint.setColor(text_color);
        paint.setTextSize(text_size);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //获得测量模式和大小
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        //存储wrap_content模式下的宽高
        int wrap_width = 0;
        int wrap_height = 0;

        for (int i = 0; i < Constant.letter.length; i++) {
            //获取字母绘制出来的宽度
            Rect rect = new Rect();
            paint.getTextBounds(Constant.letter[i],0,1,rect);

            //测试
            Log.d(Constant.TAG,Constant.letter[i]+":"+(rect.bottom - rect.top));

            //最大宽度
            wrap_width = Math.max(wrap_width,rect.right - rect.left);
            max_letter_height = Math.max(max_letter_height,rect.bottom - rect.top);
        }

        //累加间距
        wrap_width += space_hor * 2;
        wrap_height += max_letter_height * Constant.letter.length + space_ver * (Constant.letter.length + 1);

        //根据测量模式去保存相应的测量宽度
        //即如果是MeasureSpec.EXACTLY直接使用父ViewGroup传入的宽和高
        //否则设置为自己计算的宽和高，即为warp_content时
        setMeasuredDimension(
                (widthMode == MeasureSpec.EXACTLY) ? widthSize : wrap_width,
                (heightMode == MeasureSpec.EXACTLY) ? heightSize : wrap_height
        );

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取宽高
        int width = getWidth();
        int height = getHeight();

        //计算绘制的baseline往下的偏移距离 baseline的坐标是0
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float base_space = fontMetrics.bottom;
        Log.d(Constant.TAG,"top:"+fontMetrics.top);
        Log.d(Constant.TAG,"ascent:"+fontMetrics.ascent);
        Log.d(Constant.TAG,"descent:"+fontMetrics.descent);
        Log.d(Constant.TAG,"bottom:"+fontMetrics.bottom);
        Log.d(Constant.TAG,"leading:"+fontMetrics.leading);

        //绘制每一个字母
        for (int i = 0; i < Constant.letter.length; i++) {
            //1.获取每一个字母的高度
            Rect rect = new Rect();
            paint.getTextBounds(Constant.letter[i],0,1,rect);
            int letter_width = rect.right - rect.left;
            int letter_height = rect.bottom - rect.top;

            //2.存储绘制文本的起始坐标
            float x;
            float y;

            //3.获取绘制的x起始点
            x = (width - letter_width) >> 1;

            //4.获取绘制的y起始点
            float distance = ((fontMetrics.bottom - fontMetrics.top) / 2) - fontMetrics.bottom;
            float location = ((max_letter_height - letter_height) >> 1) + (letter_height >> 1) + distance;
            y = space_ver + location + (space_ver + max_letter_height) * i;

            //5.绘制文本
            if (TextUtils.equals(currentLetter,Constant.letter[i])){
                paint.setColor(select_text_color);

                canvas.drawText(
                        Constant.letter[i],
                        x,
                        y,
                        paint
                );

                paint.setColor(text_color);
            }else {
                canvas.drawText(
                        Constant.letter[i],
                        x,
                        y,
                        paint
                );
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //获取触摸点的坐标x,y
        float x = event.getX();
        float y = event.getY();

        //获取选中的字母
        float position =  (y / (space_ver + max_letter_height));
        Log.d(Constant.TAG,"position:"+position);
        if (position >= 0 && position < Constant.letter.length) {
            currentLetter = Constant.letter[(int) Math.ceil(position)-1];
        }else {
            currentLetter = "";
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (TextUtils.equals(currentLetter,lastLetter)){
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                //记录上一次的字母
                lastLetter = currentLetter;
                return true;
        }

        //回调
        if (listener != null) {
            listener.currentLetter(currentLetter);
        }

        //刷新
        invalidate();

        return true;
    }

    /**
     * 回调当前的字母
     */
    public interface LetterChangeListener {
        void currentLetter(String letter);
    }

    //设置监听者
    public void setLetterChangeListener(LetterChangeListener listener) {
        this.listener = listener;
    }

    //获取当前的字母
    public String getCurrentLetter() {
        return currentLetter;
    }

    //设置当前的字母
    public void setCurrentLetter(String currentLetter) {
        this.currentLetter = currentLetter;

        invalidate();
    }
}
