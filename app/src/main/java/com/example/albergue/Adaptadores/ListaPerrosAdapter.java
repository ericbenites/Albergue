package com.example.albergue.Adaptadores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.albergue.Dto.MascotasRegistro;
import com.example.albergue.RescatadoDetalle;
import com.example.albergue.R;
import com.example.albergue.Users.RescatadoDetalleUsuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ListaPerrosAdapter extends RecyclerView.Adapter<ListaPerrosAdapter.ViewHolder> {


    private List<MascotasRegistro> listaPrros;
    private Context context;

    public ListaPerrosAdapter(List<MascotasRegistro> listaPrros, Context context) {
        this.listaPrros = listaPrros;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.listaperros, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FirebaseUser usuario = FirebaseAuth.getInstance().getCurrentUser();
        String uid = usuario.getUid();
        final MascotasRegistro mascotasRegistro = listaPrros.get(position);
        StorageReference storageReference = FirebaseStorage.getInstance()
                .getReference().child("Perros").child(mascotasRegistro.getIdRescatado() + ".jpg");
        Glide.with(holder.imagenRescatado.getContext()).load(storageReference).into(holder.imagenRescatado);
        holder.nombre.setText(mascotasRegistro.getNombre());

        holder.conocemas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uid.equals("W0pfxu2AwPd51rytX7Lb1jVhXKF2")){
                    Intent intent = new Intent(context, RescatadoDetalle.class);
                    intent.putExtra("nombre", mascotasRegistro.getNombre());
                    intent.putExtra("peso", mascotasRegistro.getPeso());
                    intent.putExtra("raza", mascotasRegistro.getRaza());
                    intent.putExtra("adicional", mascotasRegistro.getAdicional());
                    intent.putExtra("fecha", mascotasRegistro.getFecha());
                    intent.putExtra("idRescatado", mascotasRegistro.getIdRescatado());
                    intent.putExtra("tiporescatado", "Perro" );
                    intent.putExtra("tipo1", 5);
                    context.startActivity(intent);
                }else {
                    Intent intent = new Intent(context, RescatadoDetalleUsuario.class);
                    intent.putExtra("nombre", mascotasRegistro.getNombre());
                    intent.putExtra("peso", mascotasRegistro.getPeso());
                    intent.putExtra("raza", mascotasRegistro.getRaza());
                    intent.putExtra("adicional", mascotasRegistro.getAdicional());
                    intent.putExtra("fecha", mascotasRegistro.getFecha());
                    intent.putExtra("idRescatado", mascotasRegistro.getIdRescatado());
                    intent.putExtra("tiporescatado", "Perro" );
                    intent.putExtra("tipo1", 5);
                    context.startActivity(intent);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
        return listaPrros.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nombre;
        Button conocemas;
        ImageView imagenRescatado;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre = itemView.findViewById(R.id.nombrePerroRecy);
            conocemas = itemView.findViewById(R.id.buttonRecyPerro);
            imagenRescatado = itemView.findViewById(R.id.imagePerro);

        }
    }

}
