package gui;

/**
 * Created by mversaggi on 5/11/2017.
 */
public enum ShapeType
{
    SQUARE("Square"), CIRCLE("Circle");

    private String displayName;

    ShapeType(String displayName)
    {
        this.displayName = displayName;
    }

    @Override
    public String toString()
    {
        return displayName;
    }
}
