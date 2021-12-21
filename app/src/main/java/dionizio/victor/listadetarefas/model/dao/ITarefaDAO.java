package dionizio.victor.listadetarefas.model.dao;

import java.util.List;

import dionizio.victor.listadetarefas.model.Tarefa;

public interface ITarefaDAO {
    public boolean salvar(Tarefa tarefa);
    public boolean atualizar(Tarefa tarefa);
    public boolean deletar(Tarefa tarefa);
    public List<Tarefa> listar();
}
