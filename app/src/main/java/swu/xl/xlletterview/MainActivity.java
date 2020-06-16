package swu.xl.xlletterview;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        XLLetterView letterView = findViewById(R.id.letter);

        letterView.setLetterChangeListener(new XLLetterView.LetterChangeListener() {
            @Override
            public void currentLetter(String letter) {
                Toast.makeText(MainActivity.this, "当前的字母："+letter, Toast.LENGTH_SHORT).show();
            }
        });

        letterView.setCurrentLetter("S");
    }
}