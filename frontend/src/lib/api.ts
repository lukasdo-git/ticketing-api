import { CreateTicketInput, Status, Ticket } from "./types";

const API_URL = process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8808";

async function handleResponse<T>(res: Response): Promise<T> {
  if (!res.ok) {
    const body = await res.text();
    let message = `Request failed with status ${res.status}`;
    if (body) {
      try {
        const parsed = JSON.parse(body);
        if (typeof parsed === "string") {
          message = parsed;
        } else if (parsed && typeof parsed === "object") {
          message = Object.values(parsed).join(", ");
        }
      } catch {
        message = body;
      }
    }
    throw new Error(message);
  }
  if (res.status === 204) {
    return undefined as T;
  }
  return (await res.json()) as T;
}

export function getTickets(): Promise<Ticket[]> {
  return fetch(`${API_URL}/tickets`, { cache: "no-store" }).then((res) =>
    handleResponse<Ticket[]>(res),
  );
}

export function getTicket(id: number): Promise<Ticket> {
  return fetch(`${API_URL}/tickets/${id}`, { cache: "no-store" }).then((res) =>
    handleResponse<Ticket>(res),
  );
}

export function createTicket(input: CreateTicketInput): Promise<Ticket> {
  return fetch(`${API_URL}/tickets`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(input),
  }).then((res) => handleResponse<Ticket>(res));
}

export function updateTicketStatus(id: number, status: Status): Promise<Ticket> {
  return fetch(`${API_URL}/tickets/${id}/status`, {
    method: "PATCH",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ status }),
  }).then((res) => handleResponse<Ticket>(res));
}

export function deleteTicket(id: number): Promise<void> {
  return fetch(`${API_URL}/tickets/${id}`, { method: "DELETE" }).then((res) =>
    handleResponse<void>(res),
  );
}
