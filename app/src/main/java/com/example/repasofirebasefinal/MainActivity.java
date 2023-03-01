package com.example.repasofirebasefinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText txtfrase;
    private TextView lblfrase;
    private Button btnsave;
    private ArrayList<Persona> personas;

    private FirebaseDatabase dt ;

    private DatabaseReference refFrase;

    private DatabaseReference refPersona;
    private DatabaseReference refPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtfrase = findViewById(R.id.txtFrase);
        lblfrase = findViewById(R.id.lblfrase);
        btnsave = findViewById(R.id.btsave);

        dt = FirebaseDatabase
                .getInstance("https://repasofirebasefinal-default-rtdb.europe-west1.firebasedatabase.app/");

// inicializaciones
        // como se ataca a la base de datos
        // si se la ruta que quiero acceder  lo ponemos dentro del parentesis
        personas = new ArrayList<>();
        crearPersonas();
        refFrase = dt.getReference("frase");
        refPersona = dt.getReference("persona");
        refPersonas = dt.getReference("personas");

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //escrituras
                refFrase.setValue(txtfrase.getText().toString());
                int random = (int)(Math.random()*100);
                Persona p = new Persona (txtfrase.getText().toString(), random);
                refPersona.setValue(p);

                //como guardo esa lista:

                refPersonas.setValue(personas);
            }
        });
        //LEcturas
        /*
        a la refenrencia de frase agregare una valuelisten implica que cuando inicia la app va a hacer una primera
        descarga para saber que datos he guardado y se quedara comprobando para que solo compruebe una vez
        le ponemos el single valueevent lisent

         */
        //ver como la traigo y hay un problema cuando quiero hacer un value event liste sobre la refe que contien una lista
        // cuando me aseuro que el snapshot existe y ademas me intento traer un arrylist de objetos a partir del snap cuando le pongo como antes el .class
        // no es una class sino una coleccion da error entonces usar la clase propia de firebase generatetypeindica para generer un objeto de ese tipo
        // de modo dentro del elemento tengo que ponerle el modelo de datos quer tiene que mapear qe seria un arraylist instanciar una varianble
        // y crearla pero te pide que le digas que funciones quieres hacer no seleccionar ninguna y con esto se hace gti que mapea un arraylist
        //para declir al snp que tipo de datos contiene

refPersonas.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        if (snapshot.exists()){
            GenericTypeIndicator<ArrayList<Persona>> gti = new GenericTypeIndicator<ArrayList<Persona>>() {};
            ArrayList<Persona>lista = snapshot.getValue(gti);
            Toast.makeText(MainActivity.this, "Descargados: "+lista.size(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError error) {

    }
});

        refPersona.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Persona p = snapshot.getValue(Persona.class);
                    Toast.makeText(MainActivity.this, p.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        refFrase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*
                Tiene dos metodos este se dispara cuando se haga un cambio pero se dispara solo, esta en segundo plano
                pero hay ubn problema he creadoun metodo a una refencia que no se si existe eso es que google me va
                a dar soporte para saber si se ha creado en nuestro caso  esta hecho al nodo frse lee de todo tipo de datos
                 fase 1 asegurarme de que existe si ya hay un nodo que se llama frase (snapshot) es cuando tiene sentido
                  empezar a leer y cuando ya se que existe me creo un string frase que va ser
                  igual llamar al snapshop y decirle que quiero tener su valor pero dentro del getvalue tien un class
                  T es decir no sabe que tipo de datos tiene y tenemos que castearlo en este caso a un string
fase 2
                 */

                if (snapshot.exists()){
                    String frase = snapshot.getValue(String.class);
                    lblfrase.setText(frase);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
/*

 */


            }
        });


    }

    private void crearPersonas() {
        for (int i = 0; i < 100; i++) {
            personas.add(new Persona("persona"+i, (int)(Math.random()*10)));


        }
    }
}