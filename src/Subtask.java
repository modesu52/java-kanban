class Subtask extends Task {
    private final int EpicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.EpicId = epicId;
    }

    public int getEpicId() {
        return EpicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "epicId=" + getEpicId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
