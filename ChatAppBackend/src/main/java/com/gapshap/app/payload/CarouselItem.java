package com.gapshap.app.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarouselItem {

	private Long  id ;
	private String name ;
	private String avatar ;
	private boolean isActive ;
}
