package command;

import modes.TimeLimitMode;
import modes.TypingTimer;
import modes.NormalMode;
import modes.ZenMode;
import storage.AutoAdjust;
import storage.Milestones;
import storage.State;
import storage.TypingTargets;
import typing.TypingAccuracy;
import typing.TypingTargetList;
import util.TextSelector;
import ui.Ui;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class StartCommand extends Command {

    @Override
    public void execute(
            Ui ui,
            Scanner sc,
            Milestones milestones,
            TypingTimer typingTimer,
            TypingAccuracy typingAccuracy,
            TypingTargetList typingTargetList,
            TypingTargets typingTargets,
            State state,
            AutoAdjust autoAdjust
    ) throws IOException {
        while (true) {
            ui.chooseMode();
            String mode = sc.nextLine().trim();

            if (mode.equals("zen")) {
                ZenMode zenMode = new ZenMode(typingTimer, sc, ui);
                zenMode.startZenMode();
            } else if (mode.equals("timeLimit") || mode.equals("normal")) {
                TextSelector textSelector = new TextSelector(sc, ui);
                List<String> testText = textSelector.selectText();

                // time limit mode
                if (mode.equals("timeLimit")) {
                    TimeLimitMode timeLimitMode = new TimeLimitMode(ui, sc);
                    try {
                        timeLimitMode.startTimeLimitMode(testText, textSelector.getDifficultyLevel());
                    } catch (InterruptedException e) {
                        ui.showErrorMessage(e.getMessage());
                    }

                    // normal mode
                } else {
                    NormalMode normalMode = new NormalMode(
                            ui, sc, typingTargetList, typingTargets, state, autoAdjust, typingAccuracy
                    );
                    normalMode.startNormalMode(testText);
                }

                ui.showEndGame();
                break;

                // Catch exceptions
            } else {
                ui.showErrorMessage("Please enter a valid mode: 'normal', 'timeLimit', or 'zen'.");
            }
        }
    }
}
