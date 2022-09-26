package com.morisseau.gallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private final int STORAGE_PERMISSION_CODE = 23;
    private final int GALLERY_REQUEST_CODE=24;
    private Button btnGal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Autorisation d'accès au stockage externe
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);

        btnGal = (Button) findViewById(R.id.btnGallery);
        btnGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Créer un Intent avec une action  ACTION_PICK
                Intent intent=new Intent(Intent.ACTION_PICK);
                //Définissez le type comme image/*.
                //Cela garantit que seuls les composants de type image sont sélectionnés
                intent.setType("image/*");
                //Nous passons un tableau supplémentaire avec les types MIME acceptés.
                //Cela garantira que seuls les composants avec ces types MIME sont ciblés.
                String[] mimeTypes = {"image/jpeg", "image/png","image/jpg"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                //Lancer l'Intent
                startActivityForResult(intent,GALLERY_REQUEST_CODE);
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        //Le code de résultat est RESULT_OK uniquement si l'utilisateur sélectionne une image.
        ImageView imageView = (ImageView) findViewById(R.id.imgView);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode){
                case GALLERY_REQUEST_CODE:
                    //data.getData renvoie l'URI de contenu pour l'image sélectionnée
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    // Obtenez le curseur
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    // Déplacer vers la première ligne
                    cursor.moveToFirst();
                    //Obtenir l'index de colonne de MediaStore.Images.Media.DATA
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    //Obtient la valeur de chaîne dans la colonne
                    String imgDecodableString = cursor.getString(columnIndex);
                    cursor.close();
                    //Définir l'image dans ImageView après le décodage de la chaîne
                    imageView.setImageBitmap(BitmapFactory.decodeFile(imgDecodableString));
                    break;
            }
    }
}

