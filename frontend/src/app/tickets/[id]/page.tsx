"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { deleteTicket, getTicket, updateTicketStatus } from "@/lib/api";
import { Status, STATUSES, Ticket } from "@/lib/types";
import { PriorityBadge, StatusBadge } from "@/components/Badges";

export default function TicketDetailPage() {
  const params = useParams<{ id: string }>();
  const router = useRouter();
  const ticketId = Number(params.id);

  const [ticket, setTicket] = useState<Ticket | null>(null);
  const [error, setError] = useState<string | null>(null);
  const [updating, setUpdating] = useState(false);
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    getTicket(ticketId)
      .then(setTicket)
      .catch((err: Error) => setError(err.message));
  }, [ticketId]);

  async function handleStatusChange(status: Status) {
    setUpdating(true);
    setError(null);
    try {
      const updated = await updateTicketStatus(ticketId, status);
      setTicket(updated);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to update status");
    } finally {
      setUpdating(false);
    }
  }

  async function handleDelete() {
    if (!confirm("Delete this ticket? This cannot be undone.")) return;
    setDeleting(true);
    setError(null);
    try {
      await deleteTicket(ticketId);
      router.push("/");
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to delete ticket");
      setDeleting(false);
    }
  }

  if (error && !ticket) {
    return <p className="text-red-600 dark:text-red-400">Failed to load ticket: {error}</p>;
  }

  if (!ticket) {
    return <p className="text-zinc-500">Loading ticket…</p>;
  }

  return (
    <div className="flex flex-col gap-5">
      <div className="flex items-start justify-between gap-4">
        <h1 className="text-xl font-semibold text-zinc-900 dark:text-zinc-50">
          #{ticket.ticketId} {ticket.title}
        </h1>
        <div className="flex shrink-0 gap-2">
          <PriorityBadge priority={ticket.priority} />
          <StatusBadge status={ticket.status} />
        </div>
      </div>

      {ticket.description && (
        <p className="text-zinc-700 dark:text-zinc-300">{ticket.description}</p>
      )}

      <label className="flex flex-col gap-1">
        <span className="text-sm font-medium text-zinc-700 dark:text-zinc-300">
          Update status
        </span>
        <select
          value={ticket.status}
          disabled={updating}
          onChange={(e) => handleStatusChange(e.target.value as Status)}
          className="w-fit rounded-md border border-zinc-300 px-3 py-2 dark:border-zinc-700 dark:bg-zinc-900"
        >
          {STATUSES.map((s) => (
            <option key={s} value={s}>
              {s}
            </option>
          ))}
        </select>
      </label>

      {error && <p className="text-sm text-red-600 dark:text-red-400">{error}</p>}

      <button
        onClick={handleDelete}
        disabled={deleting}
        className="w-fit rounded-md border border-red-300 px-4 py-2 text-sm font-medium text-red-700 hover:bg-red-50 disabled:opacity-50 dark:border-red-900 dark:text-red-400 dark:hover:bg-red-950"
      >
        {deleting ? "Deleting…" : "Delete ticket"}
      </button>
    </div>
  );
}
