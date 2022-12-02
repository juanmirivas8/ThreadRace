import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Winners {
    public ArrayList<String> winners;
    public Winners(){
        winners = new ArrayList<>();
    }

    public void addWinner(String winner){
        winners.add(0,winner);
    }
}
