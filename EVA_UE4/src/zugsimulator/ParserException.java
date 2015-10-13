package zugsimulator;
public class ParserException extends Exception
{

    public String error;
    
    
    public ParserException()
    {
    }
    
    public ParserException(String error)
    {
        this.error = error;
    }
    
    
}