package hlccd.regular.Friend;

public class Friend_search_data {
    private int    portrait;
    private Long   id;
    private String name;
    private String type;

    Friend_search_data() {
    }

    Friend_search_data(int portrait, Long id, String name, String type) {
        this.portrait = portrait;
        this.id       = id;
        this.name     = name;
        this.type     = type;
    }

    public int getPortrait() {
        return portrait;
    }

    public void setPortrait(int portrait) {
        this.portrait = portrait;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
