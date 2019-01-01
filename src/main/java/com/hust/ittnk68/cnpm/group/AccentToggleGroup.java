package com.hust.ittnk68.cnpm.group;

import java.util.Arrays;
import java.util.List;

import atlantafx.base.theme.Styles;
import javafx.scene.control.Button;

public class AccentToggleGroup {
    List <Button> list;

    private void reset () {
        for (Button node : list) {
            node.getStyleClass().remove (Styles.ACCENT);
        }
    }

    public void setActive (Button node) {
        reset ();
        node.getStyleClass ().add (Styles.ACCENT);
    }

    public AccentToggleGroup (Button ... nodes) {
        list = Arrays.asList (nodes);
        for (Button node : list) {
            node.setOnAction (e -> {
                setActive (node);
            });
        }
    }
}
