export type Priority = "LOW" | "MEDIUM" | "HIGH" | "CRITICAL";

export type Status = "SUBMITTED" | "IN_REVIEW" | "RESOLVED";

export const PRIORITIES: Priority[] = ["LOW", "MEDIUM", "HIGH", "CRITICAL"];

export const STATUSES: Status[] = ["SUBMITTED", "IN_REVIEW", "RESOLVED"];

export interface Ticket {
  ticketId: number;
  title: string;
  description: string | null;
  status: Status;
  priority: Priority;
}

export interface CreateTicketInput {
  title: string;
  description?: string;
  priority: Priority;
}
