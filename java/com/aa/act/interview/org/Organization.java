package com.aa.act.interview.org;

import java.util.Collection;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

public abstract class Organization {

    private Position root;
    
    public Organization() {
        root = createOrganization();
    }
    
    protected abstract Position createOrganization();
    
    /**
     * hire the given person as an employee in the position that has that title
     * 
     * @param person
     * @param title
     * @return the newly filled position or empty if no position has that title
     */
    public Optional<Position> hire(Name person, String title) {
        Optional<Position> onboard = lookup(title, root);
        onboard.get().setEmployee(Optional.of(new Employee(UUID.randomUUID().toString(), person)));
        return onboard;
    }

    private Optional<Position> lookup(String title, Position current) {
        Optional<Position> rs = Optional.empty();
        if(title == current.getTitle()) {
            rs = Optional.of(current);
        } else {
            Optional<Position> subordinate = current.getDirectReports().stream().filter(p -> p.getTitle() == title).findAny();
            if(subordinate.isPresent()) {
                rs = subordinate;
            } else {
//                Stack<Position> currentRoot = new Stack<>();
                Collection<Position> currentDirectReports = current.getDirectReports();
                if(currentDirectReports.isEmpty()) {
                    return Optional.empty();
                } else {
                    for(Position d : currentDirectReports) {
                        rs = lookup(title, d);
                        if(rs.isPresent()) {
                            break;
                        }
                    }
                }
            }
        }
        return rs;
    }

    @Override
    public String toString() {
        return printOrganization(root, "");
    }

    //BFS
    private String printOrganization(Position pos, String prefix) {
        StringBuffer sb = new StringBuffer(prefix + "+-" + pos.toString() + "\n");
        for(Position p : pos.getDirectReports()) {
            sb.append(printOrganization(p, prefix + "  "));
        }
        return sb.toString();
    }
}
