package com.i4rt.easyscan.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "scan_results")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScanResults {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column
    private boolean acceptMark;

    @Column
    private Integer type;

    @Column
    private String time;

    @Column
    private Double l1;
    @Column
    private Double l2;
    @Column
    private Double l3;

    @Column
    private Double h1;
    @Column
    private Double h2;
    @Column
    private Double h3;

    @Column
    private Double maxDeviation;
    @Column
    private Double minDeviation;

    public ScanResults(boolean acceptMark, Integer type, String time, Double l1, Double l2, Double l3, Double h1, Double h2, Double h3, Double maxDeviation, Double minDeviation) {
        this.acceptMark = acceptMark;
        this.type = type;
        this.time = time;
        this.l1 = l1;
        this.l2 = l2;
        this.l3 = l3;
        this.h1 = h1;
        this.h2 = h2;
        this.h3 = h3;
        this.maxDeviation = maxDeviation;
        this.minDeviation = minDeviation;
    }

    public String getHTML(){
        return "<div><div><b>ID" + id + "</b></div></div>" +
                "<div><div><b>Тип " + type + "</b></div></div>" +
                "<div><div><div>Время:</div><div>" + time + "</div></div></div>" +
                "<div><div><h3>" + (acceptMark?"ОДОБРЕНО":"ОТКЛАНЕНО") + "</h3></div></div>" +
                "<div>" +
                    "<div>" +
                        "<div>Высота (h1):</div>"  + "<div>" + h1 + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Высота (h2):</div>"  + "<div>" + h2 + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Высота (h3):</div>"  + "<div>" + h3 + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Диапазон</div>"  + "<div>" + Math.min(h1, Math.min(h2, h3)) + " - " + Math.max(h1, Math.max(h2, h3)) + "</div>" +
                    "</div>" +
                "</div>" +
                "<div>" +
                    "<div>" +
                        "<div>Длина (l1):</div>"  + "<div>" + l1 + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Длина (l2):</div>"  + "<div>" + l2 + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Длина (l3):</div>"  + "<div>" + l3 + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Диапазон</div>"  + "<div>" + Math.min(l1, Math.min(l2, l3)) + " - " + Math.max(l1, Math.max(l2, l3)) + "</div>" +
                    "</div>" +
                "</div>" +
                "<div>" +
                    "<div>" +
                        "<div>Максимальное отклонение:</div>"  + "<div>" + maxDeviation + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Минимальное отклонение:</div>"  + "<div>" + minDeviation + "</div>" +
                    "</div>" +
                    "<div>" +
                        "<div>Диапазон</div>"  + "<div>" + minDeviation + " - " + maxDeviation + "</div>" +
                    "</div>" +
                "</div>";
    }

}
