package triicky_pc.example.com.snapchat;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnItemSelectedListener {

    private static final int PICTURE_RESULT = 9;

    private Bitmap mPicture;
    private ImageView mView;

    Spinner spinner;
    String spinner_text;
    TextView testdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);

        //testdate = (TextView) findViewById(R.id.testdate);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.date_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);


        TextView photo = (TextView) findViewById(R.id.photo);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photo = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // on lance l'activité en précisant qu'elle va nous renvoyer un résultat image.
                MainActivity.this.startActivityForResult(photo, PICTURE_RESULT);
            }
        });

        mView = (ImageView) findViewById(R.id.view);
    }

    @Override
    // lorsque l'action précédente a rendu un résultat, on le traite.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // if results comes from the camera activity
        if (requestCode == PICTURE_RESULT) {

            // if a picture was taken
            if (resultCode == Activity.RESULT_OK) {
                // Free the data of the last picture

                if(mPicture != null)
                    mPicture.recycle();

                // Get the picture taken by the user
                mPicture = (Bitmap) data.getExtras().get("data");

                // Avoid IllegalStateException with Immutable bitmap
                Bitmap pic = mPicture.copy(mPicture.getConfig(), true);
                mPicture.recycle();
                mPicture = pic;

                // Affiche l'image sur l'interface
                mView.setImageBitmap(mPicture);

                // ATTENTION POUR INFORMATION : la photo fait 235px de large sur 140px de haut!
                int width = mPicture.getWidth();
                int height = mPicture.getHeight();
                Toast.makeText(getApplicationContext(), "Taille de l'image: Width: " + width + "; Height: " + height,
                        Toast.LENGTH_LONG).show();

                // if user canceled from the camera activity
            } else if (resultCode == Activity.RESULT_CANCELED) { }

        } // fin if requestedCode vient bien de l'appareil photo

    }  // fin de onActivityResult

    public void onDestroy() {
        super.onDestroy();
        if (mPicture != null) {
            mPicture.recycle();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinner_text = spinner.getSelectedItem().toString();
        Toast.makeText(MainActivity.this, "Le délai a été fixé à " + spinner_text + " secondes.", Toast.LENGTH_SHORT).show();

        //récupère la valeur
        //testdate.setText(spinner_text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
