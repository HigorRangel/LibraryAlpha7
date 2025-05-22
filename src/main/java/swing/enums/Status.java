package swing.enums;

public enum Status {
    ACTIVE('A'),
    INACTIVE('I');

    private final Character status;

    Status(Character status) {
        if (status == null) {
            throw new IllegalArgumentException("Status cannot be null");
        }
        this.status = status;
    }

    public Character getStatus() {
        return status;
    }

    public static Status fromCharacter(String status) {
        if (status == null) {
            return null;
        }
        Character statusChar = status.charAt(0);
        for (Status s : Status.values()) {
            if (s.getStatus().equals(statusChar)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }

    @Override
    public String toString() {
        return status == null ? null : status.toString();
    }
}
