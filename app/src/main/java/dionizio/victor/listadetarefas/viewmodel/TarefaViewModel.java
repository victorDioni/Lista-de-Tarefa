package dionizio.victor.listadetarefas.viewmodel;

import static android.os.Build.VERSION_CODES.R;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import dionizio.victor.listadetarefas.R;
import dionizio.victor.listadetarefas.model.Tarefa;
import dionizio.victor.listadetarefas.model.dao.TarefaDAO;
import dionizio.victor.listadetarefas.model.helper.RecyclerItemClickListener;
import dionizio.victor.listadetarefas.view.AdicionarTarefaActivity;
import dionizio.victor.listadetarefas.viewmodel.adapter.TarefaAdapter;

public class TarefaViewModel extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TarefaAdapter tarefaAdapter;
    private List<Tarefa> listaTarefas = new ArrayList<>();
    private Tarefa tarefaSelecionada;

    public void recyclerViewClick(){
        recyclerView = findViewById(dionizio.victor.listadetarefas.R.id.recyclerViewTarefa);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerView,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                recuperarTarefaEditar(position);
                                enviaTarefaTelaAddTarefa();
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {
                                deletarTarefa(position);
                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
    }

    public void carregarListaTarefas(){
        //Listar tarefas
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());
        listaTarefas = tarefaDAO.listar();

        tarefaAdapter = new TarefaAdapter(listaTarefas);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(tarefaAdapter);
    }

    @Override
    protected void onStart() {
        carregarListaTarefas();
        super.onStart();
    }

    public void recuperarTarefaEditar(int position){
        // Recuperar tarefa para editar
        Tarefa tarefaSelecionada = listaTarefas.get(position);
    }

    public void enviaTarefaTelaAddTarefa(){
        //Envia tarefa para tela adicionar tarefa
        Intent intent = new Intent(TarefaViewModel.this, AdicionarTarefaActivity.class);
        intent.putExtra("tarefaSelecionada", tarefaSelecionada);

        startActivity(intent);
    }

    public void toast(int text){
        Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT).show();
    }

    public void deletarTarefa(int position){
        // Recuperar tarefa para deletar
        tarefaSelecionada = listaTarefas.get(position);

        AlertDialog.Builder dialog = new AlertDialog.Builder(TarefaViewModel.this);

        // Configura titulo e mensagem
        dialog.setTitle(dionizio.victor.listadetarefas.R.string.confirmar_exclusao);
        dialog.setMessage(dionizio.victor.listadetarefas.R.string.excluir_tarefa +
                tarefaSelecionada.getNomeTarefa() + "?");

        dialog.setPositiveButton(dionizio.victor.listadetarefas.R.string.sim,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

                        if(tarefaDAO.deletar(tarefaSelecionada)){
                            carregarListaTarefas();
                            toast(dionizio.victor.listadetarefas.R.string.tarefa_deletar_sucesso);
                        }else{
                            toast(dionizio.victor.listadetarefas.R.string.tarefa_deletar_erro);
                        }
                    }
                });

        dialog.setNegativeButton(dionizio.victor.listadetarefas.R.string.nao, null);

        // Exibir dialog
        dialog.create();
        dialog.show();


    }
}
