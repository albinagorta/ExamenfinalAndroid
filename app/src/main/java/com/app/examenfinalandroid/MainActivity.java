package com.app.examenfinalandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Retrofit;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Retrofit;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    EditText etIdBuscar, etId, etNombre, etstock, etMarca, etPrecio;
    Button btnIdBuscar, btnEliminar, btnTodosBuscar, btnAgregar, btnEditar;

    ListView lvProductos;

    List<Producto> listaProductos = new ArrayList<>();
    Producto p;
    Retrofit retrofit;
    APIRest api;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        etIdBuscar = findViewById(R.id.etIdBuscar);
        etId = findViewById(R.id.etCod);
        etNombre = findViewById(R.id.etNombr);
        etstock = findViewById(R.id.etstock);
        etMarca = findViewById(R.id.etmarc);
        etPrecio = findViewById(R.id.etpre);
        btnIdBuscar = findViewById(R.id.btnIdBuscar);
        btnEliminar = findViewById(R.id.btnEliminar);
        btnTodosBuscar = findViewById(R.id.btnTodosBuscar);
        btnAgregar = findViewById(R.id.btnAgregar);
        btnEditar = findViewById(R.id.btnEditar);
        lvProductos = findViewById(R.id.lvProducto);

        retrofit = new AdaptadorRetrofit().getAdapter();
        api = retrofit.create(APIRest.class);

        getArticulos(api);

        btnIdBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etIdBuscar.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Inserta un CODIGO del producto para buscar", Toast.LENGTH_SHORT).show();
                } else {
                    getArticulo(api, etIdBuscar.getText().toString());
                }
            }
        });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etIdBuscar.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Inserta un CODIGO del producto para eliminar", Toast.LENGTH_SHORT).show();
                } else {
                    eliminarArticulo(api, etIdBuscar.getText().toString());
                }
            }
        });

        btnTodosBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getArticulos(api);
            }
        });

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etNombre.getText().toString().equals("") || etstock.getText().toString().equals("") || etMarca.getText().toString().equals("") || etPrecio.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Se deben de llenar los campos", Toast.LENGTH_SHORT).show();
                } else {
                    agregarArticulo(api, etNombre.getText().toString(), etMarca.getText().toString(), etPrecio.getText().toString(), etstock.getText().toString());
                }
            }
        });

        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etId.getText().toString().equals("") || etNombre.getText().toString().equals("") || etstock.getText().toString().equals("") || etMarca.getText().toString().equals("") || etPrecio.getText().toString().equals("")) {
                    Toast.makeText(getApplication(), "Se deben de llenar los campos", Toast.LENGTH_SHORT).show();
                } else {
                    editarArticulo(api, etId.getText().toString(), etNombre.getText().toString(), etMarca.getText().toString(), etPrecio.getText().toString(), etstock.getText().toString());
                }
            }
        });

        lvProductos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                p=listaProductos.get(position);
                etId.setText(p.getCodigo());
                etNombre.setText(p.getNombre());
                etMarca.setText(p.getMarca());
                etPrecio.setText(p.getPrecio());
                etstock.setText(p.getStock());
            }
        });




    }

    public void getArticulo(final APIRest api, String id) {
        listaProductos.clear();
        Call<Producto> call = api.obtenerProducto(id);

        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                switch (response.code()) {
                    case 200:
                        listaProductos.add(response.body());

                        etIdBuscar.setText("");

                        ArrayList<String> lista=new ArrayList<>();
                        for(Producto a:listaProductos){
                            lista.add(a.getCodigo()+" "+a.getNombre()+" "+a.getMarca()+" "+a.getPrecio()+" "+a.getStock());
                        }

                        ArrayAdapter adapter=new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,lista);
                        lvProductos.setAdapter(adapter);

                        break;
                    case 204:
                        Toast.makeText(getApplication(), "No existe ese registro", Toast.LENGTH_SHORT).show();

                        etIdBuscar.setText("");

                        getArticulos(api);
                        break;

                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {

            }
        });
    }

    public void getArticulos(APIRest api) {
        listaProductos.clear();
        Call<List<Producto>> call = api.obtenerProducto();

        call.enqueue(new Callback<List<Producto>>() {
            @Override
            public void onResponse(Call<List<Producto>> call, Response<List<Producto>> response) {
                listaProductos = new ArrayList<Producto>(response.body());

                ArrayList<String> lista=new ArrayList<>();
                for(Producto a:listaProductos){
                    lista.add(a.getCodigo()+" "+a.getNombre()+" "+a.getMarca()+" "+a.getPrecio()+" "+a.getStock());
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplication(),android.R.layout.simple_list_item_1,lista);
                lvProductos.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<List<Producto>> call, Throwable t) {

            }
        });
    }

    public void eliminarArticulo(final APIRest api, String id) {
        listaProductos.clear();

        Call<Void> call = api.eliminarProducto(id);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 200:
                        Toast.makeText(getApplication(), "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                        etIdBuscar.setText("");
                        getArticulos(api);
                        break;
                    case 204:
                        Toast.makeText(getApplication(), "No se elimino el registro", Toast.LENGTH_SHORT).show();
                        etIdBuscar.setText("");
                        break;

                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void agregarArticulo(final APIRest api, String nombre, String marca, String precio, String stock) {
        listaProductos.clear();
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setPrecio(precio);
        producto.setStock(stock);

        Call<Void> call = api.agregarArticulo(producto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 400:
                        Toast.makeText(getApplication(), "Faltaron campos.", Toast.LENGTH_SHORT).show();
                        etNombre.setText("");
                        etstock.setText("");
                        etMarca.setText("");
                        etPrecio.setText("");
                        break;
                    case 200:
                        Toast.makeText(getApplication(), "Se inserto correctamente", Toast.LENGTH_SHORT).show();
                        etNombre.setText("");
                        etstock.setText("");
                        etMarca.setText("");
                        etPrecio.setText("");
                        getArticulos(api);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    public void editarArticulo(final APIRest api, String id, String nombre, String marca, String precio, String stock) {
        listaProductos.clear();
        Producto producto = new Producto();
        producto.setCodigo(id);
        producto.setNombre(nombre);
        producto.setMarca(marca);
        producto.setPrecio(precio);
        producto.setStock(stock);

        Call<Void> call = api.editarProducto(producto);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                switch (response.code()) {
                    case 400:
                        Toast.makeText(getApplication(), "No se puede editar.", Toast.LENGTH_SHORT).show();
                        etId.setText("");
                        etNombre.setText("");
                        etstock.setText("");
                        etMarca.setText("");
                        etPrecio.setText("");
                        break;
                    case 200:
                        Toast.makeText(getApplication(), "Se edito correctamente", Toast.LENGTH_SHORT).show();
                        etId.setText("");
                        etNombre.setText("");
                        etstock.setText("");
                        etMarca.setText("");
                        etPrecio.setText("");
                        getArticulos(api);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}