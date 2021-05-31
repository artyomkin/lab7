package common;

import common.dataTransferObjects.CommandTransferObject;

import java.io.Serializable;

public class Response implements Serializable {
    private static final long serialVersionUID = 2L;
    private String content;
    private boolean executionFailed;
    private Instruction instruction;
    private Stage stage;
    private Query query;

    public Response (){
        this.content = "";
        this.executionFailed = false;
        this.instruction = Instruction.ASK_COMMAND;
        this.stage = Stage.BEGINNING;
        this.query = new Query();
    }
    public Response (String content, boolean failed, Instruction instruction){
        this.content = content;
        this.executionFailed = failed;
        this.instruction = instruction;
        this.stage = Stage.BEGINNING;
        this.query = new Query();
    }
    public Response (String content, boolean failed, Instruction instruction, Stage stage, Query query){
        this.content = content;
        this.executionFailed = failed;
        this.instruction = instruction;
        this.stage = stage;
        this.query = query;
    }
    public Response (String content, boolean failed, Instruction instruction, CommandTransferObject command, Stage stage, Query query){
        this.content = content;
        this.executionFailed = failed;
        this.instruction = instruction;
        this.stage = stage;
        this.query = query;
    }
    public Instruction getInstruction() {
        return instruction;
    }

    public String getContent() {
        return content;
    }

    public boolean failed() {
        return executionFailed;
    }

    public Stage getStage(){return stage;}

    public Query getQuery() { return query; }

    public Response setQuery(Query query){
        this.query = query;
        return this;
    }

    public Response setContent(String content){
        this.content = content;
        return this;
    }
    public Response setExecutionFailed(boolean flag){
        this.executionFailed = flag;
        return this;
    }
    public Response setInstruction(Instruction instruction){
        this.instruction = instruction;
        return this;
    }
}
