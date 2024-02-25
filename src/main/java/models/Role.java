

package models;



public class Role {
    private int id_role;
    private String role ;

    public Role() {
    }

    public Role(String type) {
        this.role = role;
    }

    public Role(int id_role) {
        this.id_role = id_role;
    }

    public Role(int id_role, String type) {
        this.id_role = id_role;
        this.role = role;
    }

    public int getId_role() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role = id_role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String type) {
        this.role = type;
    }

    @Override
    public String toString() {
        return "role{" + "id_role=" + id_role + ", type=" + role + '}';
    }


}