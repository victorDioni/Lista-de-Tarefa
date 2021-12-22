package dionizio.victor.listadetarefas.viewmodel;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import dionizio.victor.listadetarefas.R;
import dionizio.victor.listadetarefas.model.Tarefa;
import dionizio.victor.listadetarefas.model.dao.TarefaDAO;

public class AdicionarTarefaViewModel extends AppCompatActivity {
    private TextInputEditText editTarefa;
    private Tarefa tarefaAtual;
    private TarefaViewModel tarefaViewModel;

    public void recuperarTarefaEditar(){
        tarefaAtual = (Tarefa) getIntent().getSerializableExtra("tarefaSelecionada");
    }

    public void verificarTarefa(){
        editTarefa = findViewById(R.id.txtTarefa);

        if(tarefaAtual != null){
            editTarefa.setText(tarefaAtual.getNomeTarefa());
        }
    }

    public void editarSalvarTarefa(){
        // Ação para salvar item
        TarefaDAO tarefaDAO = new TarefaDAO(getApplicationContext());

        if(tarefaAtual != null) {//editar
            String nomeTarefa = editTarefa.getText().toString();

            if(!nomeTarefa.isEmpty()){
                Tarefa tarefa = new Tarefa();
                tarefa.setNomeTarefa(nomeTarefa);
                tarefa.setId(tarefaAtual.getId());

                // Atualizar no BD
                if(tarefaDAO.atualizar(tarefa)){
                    finish();
                    tarefaViewModel.toast(R.string.tarefa_atualizada_sucesso);
                }else {
                    tarefaViewModel.toast(R.string.tarefa_atualizada_erro);
                }
            }
        }else {//salvar
            String nomeTarefa = editTarefa.getText().toString();

            if(!nomeTarefa.isEmpty()) {
                Tarefa tarefa = new Tarefa();
                tarefa.setNomeTarefa(nomeTarefa);

                if(tarefaDAO.salvar(tarefa)){
                    finish();
                    tarefaViewModel.toast(R.string.tarefa_salva_sucesso);
                }else {
                    tarefaViewModel.toast(R.string.tarefa_salva_erro);
                }
            }

        }
    }
}
