package jpa.learn.beans.extra;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PageDetail {

	private byte page;
	private byte size;
	private String sortBy; // sortBy-> Entity's field name
	private String sortDir; //Sorting direction-> Asc, Desc
}
