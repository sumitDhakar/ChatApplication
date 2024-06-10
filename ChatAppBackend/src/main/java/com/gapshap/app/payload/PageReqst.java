package com.gapshap.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageReqst {

	private Integer pageNo;
	private Integer pageSize;
	
}
