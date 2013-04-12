package com.grahamedgecombe.tinybasic.ast;

import java.util.Objects;

public final class BranchStatement extends Statement {

    private final BranchType type;
    private final int target;

    public BranchStatement(BranchType type, int target) {
        this.type = type;
        this.target = target;
    }

    public BranchType getType() {
        return type;
    }

    public int getTarget() {
        return target;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BranchStatement that = (BranchStatement) o;

        if (target != that.target) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, target);
    }

    @Override
    public String toString() {
        return type + " " + target;
    }

}
