package api.dieti2024.testFiltri;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoppiaValoriTest {
    private int identificatore;
    private List<String> valori;
}
