package org.ebndrnk.leverxfinalproject.util.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorInfo {

	private LocalDateTime timestamp;
	private int status;
	private String error;
	private String message;
	private String path;
}
