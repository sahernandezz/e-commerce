'use client'

import { Dialog, DialogBackdrop, DialogPanel } from '@headlessui/react';
import { Auth } from '@/components/Auth';
import { useLoginModal } from '@/context/login-modal';

export const LoginModal = () => {
  const { open, setOpen } = useLoginModal();

  return (
    <Dialog open={open} onClose={setOpen} className="relative z-10">
      <DialogBackdrop className="fixed inset-0 bg-black/30 backdrop-blur-sm transition-opacity" />
      <div className="fixed inset-0 flex items-center justify-center p-4">
        <DialogPanel className="w-full max-w-sm rounded bg-white p-6 text-black shadow-lg dark:bg-neutral-900 dark:text-white">
          <div className="flex justify-end mb-2">
            <button onClick={() => setOpen(false)} aria-label="Close login">
              <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" className="h-5 transition-all ease-in-out hover:scale-110">
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18 18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
          <Auth />
        </DialogPanel>
      </div>
    </Dialog>
  );
};
