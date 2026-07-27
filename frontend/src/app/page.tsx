"use client";

import Link from "next/link";
import { useEffect, useState } from "react";
import { getTickets } from "@/lib/api";
import { Ticket } from "@/lib/types";
import { PriorityBadge, StatusBadge } from "@/components/Badges";

export default function Home() {
  const [tickets, setTickets] = useState<Ticket[] | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    getTickets()
      .then(setTickets)
      .catch((err: Error) => setError(err.message));
  }, []);

  if (error) {
    return <p className="text-red-600 dark:text-red-400">Failed to load tickets: {error}</p>;
  }

  if (!tickets) {
    return <p className="text-zinc-500">Loading tickets…</p>;
  }

  if (tickets.length === 0) {
    return (
      <div className="text-center text-zinc-500">
        <p>No tickets yet.</p>
        <Link href="/tickets/new" className="text-zinc-900 underline dark:text-zinc-50">
          Create the first one
        </Link>
      </div>
    );
  }

  return (
    <ul className="flex flex-col gap-3">
      {tickets.map((ticket) => (
        <li key={ticket.ticketId}>
          <Link
            href={`/tickets/${ticket.ticketId}`}
            className="flex flex-col gap-2 rounded-lg border border-zinc-200 bg-white p-4 hover:border-zinc-400 dark:border-zinc-800 dark:bg-zinc-900 dark:hover:border-zinc-600"
          >
            <div className="flex items-center justify-between gap-2">
              <span className="font-medium text-zinc-900 dark:text-zinc-50">
                #{ticket.ticketId} {ticket.title}
              </span>
              <div className="flex shrink-0 gap-2">
                <PriorityBadge priority={ticket.priority} />
                <StatusBadge status={ticket.status} />
              </div>
            </div>
            {ticket.description && (
              <p className="line-clamp-2 text-sm text-zinc-600 dark:text-zinc-400">
                {ticket.description}
              </p>
            )}
          </Link>
        </li>
      ))}
    </ul>
  );
}
