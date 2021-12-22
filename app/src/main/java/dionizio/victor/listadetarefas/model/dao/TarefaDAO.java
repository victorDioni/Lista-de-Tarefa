package dionizio.victor.listadetarefas.model.dao;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dionizio.victor.listadetarefas.R;
import dionizio.victor.listadetarefas.model.Tarefa;
import dionizio.victor.listadetarefas.model.helper.DBHelper;

public class TarefaDAO implements ITarefaDAO{

    private SQLiteDatabase escreve;
    private SQLiteDatabase le;

    public TarefaDAO(Context context) {
        DBHelper db = new DBHelper(context);
        escreve = db.getWritableDatabase();
        le = db.getReadableDatabase();
    }

    @Override
    public boolean salvar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());

        try{
            escreve.insert(DBHelper.TABELA_TAREFAS, null, cv);
            Log.i(String.valueOf(R.string.info), String.valueOf(R.string.tarefa_salva_sucesso));
        }catch (Exception e){
            Log.i(String.valueOf(R.string.info), String.valueOf(R.string.tarefa_salva_erro));
            return false;
        }
        return true;
    }

    @Override
    public boolean atualizar(Tarefa tarefa) {
        ContentValues cv = new ContentValues();
        cv.put("nome", tarefa.getNomeTarefa());

        try{
            String[] args = {tarefa.getId().toString()};
            escreve.update(DBHelper.TABELA_TAREFAS, cv, "id = ?", args);
            Log.i(String.valueOf(R.string.info), String.valueOf(R.string.tarefa_atualizada_sucesso));
        }catch (Exception e){
            Log.i(String.valueOf(R.string.info), String.valueOf(R.string.tarefa_atualizada_erro));
            return false;
        }
        return true;
    }

    @Override
    public boolean deletar(Tarefa tarefa) {
        try{
            String[] args = {tarefa.getId().toString()};
            escreve.delete(DBHelper.TABELA_TAREFAS,"id = ?", args);
            Log.i(String.valueOf(R.string.info), String.valueOf(R.string.tarefa_deletar_sucesso));
        }catch (Exception e){
            Log.i(String.valueOf(R.string.info), String.valueOf(R.string.tarefa_deletar_erro));
            return false;
        }
        return true;
    }

    @Override
    public List<Tarefa> listar() {
        List<Tarefa> tarefas = new ArrayList<>();

        String sql = "SELECT * FROM " + DBHelper.TABELA_TAREFAS + " ;";
        Cursor c = le.rawQuery(sql, null);

        while (c.moveToNext()){
            Tarefa tarefa = new Tarefa();

            @SuppressLint("Range") Long id = c.getLong(c.getColumnIndex("id"));
            @SuppressLint("Range") String nome = c.getString(c.getColumnIndex("nome"));

            tarefa.setId(id);
            tarefa.setNomeTarefa(nome);

            tarefas.add(tarefa);
        }

        return tarefas;
    }
}
