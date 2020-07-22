package com.app.examenfinalandroid;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface APIRest {
    String URL_PHP="API.php";

    @GET(URL_PHP)
    Call<List<Producto>> obtenerProducto();

    @GET(URL_PHP)
    Call<Producto> obtenerProducto(
            @Query("codigo") String codigo
    );

    @POST(URL_PHP)
    Call<Void> agregarArticulo(
            @Body Producto producto
    );

    @PUT(URL_PHP)
    Call<Void> editarProducto(
            @Body Producto producto
    );

    @DELETE(URL_PHP)
    Call<Void> eliminarProducto(
            @Query("codigo") String codigo
    );
}