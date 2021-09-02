package es.ucm.gdv.engine;

import java.util.List;

public interface IInput {

    class TouchEvent{

        public TouchEvent(int id, int x, int y, TouchType type) {
            this.id = id;
            this.x = x;
            this.y = y;
            this.type = type;
        }

        public enum TouchType {
            PRESSED,        // Mouse or touch
            RELEASED,       // Mouse or touch
            MOVEMENT,       // Mouse or touch drag
            KEY_PRESSED,    // Keyboard
            KEY_RELEASED    // Keyboard
        }
        // Tipo de evento
        public TouchType type;
        // Posicion en la que ocurre el evento
        public int x, y;
        // Identificador de boton o dedo(en un gesto por ejemplo)
        public int id;
    }

    List<TouchEvent> getTouchEvents();


    class KeyCode {
        public static final int KP_0 = 48;
        public static final int KP_1 = 49;
        public static final int KP_2 = 50;
        public static final int KP_3 = 51;
        public static final int KP_4 = 52;
        public static final int KP_5 = 53;
        public static final int KP_6 = 54;
        public static final int KP_7 = 55;
        public static final int KP_8 = 56;
        public static final int KP_9 = 57;
        public static final int A = 65;
        public static final int B = 66;
        public static final int C = 67;
        public static final int D = 68;
        public static final int E = 69;
        public static final int F = 70;
        public static final int G = 71;
        public static final int H = 72;
        public static final int I = 73;
        public static final int J = 74;
        public static final int K = 75;
        public static final int L = 76;
        public static final int M = 77;
        public static final int N = 78;
        public static final int O = 79;
        public static final int P = 80;
        public static final int Q = 81;
        public static final int R = 82;
        public static final int S = 83;
        public static final int T = 84;
        public static final int U = 85;
        public static final int V = 86;
        public static final int W = 87;
        public static final int X = 88;
        public static final int Y = 89;
        public static final int Z = 90;
        public static final int NUMPAD0 = 96;
        public static final int NUMPAD1 = 97;
        public static final int NUMPAD2 = 98;
        public static final int NUMPAD3 = 99;
        public static final int NUMPAD4 = 100;
        public static final int NUMPAD5 = 101;
        public static final int NUMPAD6 = 102;
        public static final int NUMPAD7 = 103;
        public static final int NUMPAD8 = 104;
        public static final int NUMPAD9 = 105;
        public static final int F1 = 112;
        public static final int F2 = 113;
        public static final int F3 = 114;
        public static final int F4 = 115;
        public static final int F5 = 116;
        public static final int F6 = 117;
        public static final int F7 = 118;
        public static final int F8 = 119;
        public static final int F9 = 120;
        public static final int F10 = 121;
        public static final int F11 = 122;
        public static final int F12 = 123;
        public static final int F13 = 61440;
        public static final int F14 = 61441;
        public static final int F15 = 61442;
        public static final int F16 = 61443;
        public static final int F17 = 61444;
        public static final int F18 = 61445;
        public static final int F19 = 61446;
        public static final int F20 = 61447;
        public static final int F21 = 61448;
        public static final int F22 = 61449;
        public static final int F23 = 61450;
        public static final int F24 = 61451;
        public static final int KP_UP = 224;
        public static final int KP_DOWN = 225;
        public static final int KP_LEFT = 226;
        public static final int KP_RIGHT = 227;
        public static final int ENTER = 10;
        public static final int TAB = 9;
        public static final int ESCAPE = 27;
        public static final int SPACE = 32;
        public static final int LEFT = 37;
        public static final int UP = 38;
        public static final int RIGHT = 39;
        public static final int DOWN = 40;
    }

}
