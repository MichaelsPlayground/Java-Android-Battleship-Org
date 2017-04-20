package edu.utep.cs.cs4330.battleship.network.packet;

class PacketID {
    private PacketID() { }

    public static final int HOST_HANDSHAKE = 1;
    public static final int CLIENT_HANDSHAKE = 2;
    public static final int HIT = 3;
    public static final int GAMEOVER = 4;
    public static final int RESTART_REQUEST = 5;
    public static final int REQUEST_RESPONSE = 6;

    public static final int INVALID = Integer.MIN_VALUE;
}