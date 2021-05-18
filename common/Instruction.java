package common;

import java.io.Serializable;

public enum Instruction implements Serializable {
    EXIT,
    SCRIPT,
    ASK_HOUSE,
    ASK_COORDINATES,
    ASK_FLAT,
    ASK_COMMAND,
    SIGN_IN,
    SIGN_UP
}
