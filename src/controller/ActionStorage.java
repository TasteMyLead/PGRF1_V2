package controller;

import fill.Filler;
import model.Model;

import java.util.ArrayList;
import java.util.List;

public class ActionStorage {
    private final List<Action> actions;

    public ActionStorage() {
        this.actions = new ArrayList<>();
    }


    public void addAction (Action action) {
        this.actions.add(action);
    }

    public void removeAction (int index) {
        if (index < this.actions.size() && index >= 0) {
            this.actions.remove(index);
        }
    }

    public void updateAction (int index, Action action) {
        if (index < this.actions.size() && index >= 0) {
            this.actions.set(index, action);
        }
    }

    public int getSize () {
        return this.actions.size();
    }

    public Action getAction (int index) {
        if (index < this.actions.size() && index >= 0) {
            return this.actions.get(index);
        }
        return null;
    }

    public List<Model> getModels () {
        List<Model> models = new ArrayList<>();

        for(int i = 0; i < actions.size(); i++){
            if (actions.get(i) instanceof Model){
                models.add((Model) actions.get(i));
            }
        }
        return models;
    }

    public List<Integer> getModelIndexes() {
        List<Integer> indexes = new ArrayList<>();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.get(i) instanceof Model) {
                indexes.add(i);
            }
        }
        return indexes;
    }

    public List<Filler> getFillers () {
        List<Filler> fillers = new ArrayList<>();
        for (Action action : this.actions) {
            if (action instanceof Filler) {
                fillers.add((Filler) action);
            }
        }
        return fillers;
    }


    public List<Action> getActions() {
        return this.actions;
    }

    public Action getLastAction (){
        return this.actions.getLast();
    }

    public void clearActions() {
        this.actions.clear();
    }
}
