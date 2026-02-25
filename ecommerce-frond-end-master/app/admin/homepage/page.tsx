'use client';

import { useEffect, useState } from 'react';
import { useAuth } from '@/context/auth';
import {
    getHomepageConfig,
    updateHomepageConfig,
    getProductsPaginated,
    HomepageConfig,
    HomepageConfigInput,
    Product,
    setAuthToken
} from '@/lib/graphql/admin';
import { SettingsIcon, RefreshIcon, CheckIcon } from '@/components/icons';

export default function HomepageConfigPage() {
    const { token } = useAuth();
    const [config, setConfig] = useState<HomepageConfig | null>(null);
    const [products, setProducts] = useState<Product[]>([]);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [message, setMessage] = useState('');

    // Form state
    const [formData, setFormData] = useState<HomepageConfigInput>({
        featuredProductMainId: undefined,
        featuredProductSecondary1Id: undefined,
        featuredProductSecondary2Id: undefined,
        carouselProductIds: [],
        showCarousel: true,
        carouselTitle: 'Productos Destacados',
        bannerTitle: '',
        bannerSubtitle: '',
        bannerImageUrl: '',
        bannerLink: '',
        bannerEnabled: false
    });

    useEffect(() => {
        if (token) {
            setAuthToken(token);
            fetchData();
        }
    }, [token]);

    const fetchData = async () => {
        try {
            setLoading(true);
            const [configData, productsData] = await Promise.all([
                getHomepageConfig(),
                getProductsPaginated(0, 100, 'ACTIVE', undefined)
            ]);

            if (configData) {
                setConfig(configData);
                setFormData({
                    featuredProductMainId: configData.featuredProductMain?.id ? parseInt(configData.featuredProductMain.id) : undefined,
                    featuredProductSecondary1Id: configData.featuredProductSecondary1?.id ? parseInt(configData.featuredProductSecondary1.id) : undefined,
                    featuredProductSecondary2Id: configData.featuredProductSecondary2?.id ? parseInt(configData.featuredProductSecondary2.id) : undefined,
                    carouselProductIds: configData.carouselProducts?.map(p => parseInt(p.id)) || [],
                    showCarousel: configData.showCarousel ?? true,
                    carouselTitle: configData.carouselTitle || 'Productos Destacados',
                    bannerTitle: configData.bannerTitle || '',
                    bannerSubtitle: configData.bannerSubtitle || '',
                    bannerImageUrl: configData.bannerImageUrl || '',
                    bannerLink: configData.bannerLink || '',
                    bannerEnabled: configData.bannerEnabled ?? false
                });
            }

            setProducts(productsData.content || []);
        } catch (error) {
            console.error('Error fetching data:', error);
        } finally {
            setLoading(false);
        }
    };

    const handleSave = async () => {
        try {
            setSaving(true);
            setMessage('');
            const result = await updateHomepageConfig(formData);
            if (result) {
                setMessage('Configuración guardada exitosamente');
                fetchData();
            } else {
                setMessage('Error al guardar la configuración');
            }
        } catch (error) {
            setMessage('Error al guardar la configuración');
        } finally {
            setSaving(false);
            setTimeout(() => setMessage(''), 3000);
        }
    };

    const handleCarouselProductToggle = (productId: number) => {
        const currentIds = formData.carouselProductIds || [];
        if (currentIds.includes(productId)) {
            setFormData({
                ...formData,
                carouselProductIds: currentIds.filter(id => id !== productId)
            });
        } else {
            setFormData({
                ...formData,
                carouselProductIds: [...currentIds, productId]
            });
        }
    };

    if (loading) {
        return (
            <div className="flex justify-center items-center h-64">
                <div className="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-600"></div>
            </div>
        );
    }

    return (
        <div className="space-y-6">
            {/* Header */}
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-bold text-black dark:text-white">Configuración de Homepage</h1>
                    <p className="text-neutral-500 dark:text-neutral-400 mt-1">
                        Personaliza la página principal de la tienda
                    </p>
                </div>
                <div className="flex gap-2">
                    <button
                        onClick={fetchData}
                        className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-neutral-700 dark:text-neutral-300 bg-white dark:bg-neutral-800 border border-neutral-300 dark:border-neutral-600 rounded-lg hover:bg-neutral-50 dark:hover:bg-neutral-700"
                    >
                        <RefreshIcon className="w-4 h-4" />
                        Recargar
                    </button>
                    <button
                        onClick={handleSave}
                        disabled={saving}
                        className="flex items-center gap-2 px-4 py-2 text-sm font-medium text-white bg-blue-600 rounded-lg hover:bg-blue-700 disabled:opacity-50"
                    >
                        {saving ? (
                            <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                        ) : (
                            <CheckIcon className="w-4 h-4" />
                        )}
                        Guardar Cambios
                    </button>
                </div>
            </div>

            {/* Message */}
            {message && (
                <div className={`p-4 rounded-lg ${message.includes('Error') ? 'bg-red-100 text-red-700 dark:bg-red-900/30 dark:text-red-400' : 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-400'}`}>
                    {message}
                </div>
            )}

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                {/* Productos Destacados */}
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6">
                    <h2 className="text-lg font-semibold text-black dark:text-white mb-4 flex items-center gap-2">
                        <SettingsIcon className="w-5 h-5" />
                        Productos Destacados
                    </h2>

                    <div className="space-y-4">
                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Producto Principal
                            </label>
                            <select
                                value={formData.featuredProductMainId || ''}
                                onChange={(e) => setFormData({ ...formData, featuredProductMainId: e.target.value ? parseInt(e.target.value) : undefined })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                            >
                                <option value="">Seleccionar producto</option>
                                {products.map(p => (
                                    <option key={p.id} value={p.id}>{p.name}</option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Producto Secundario 1
                            </label>
                            <select
                                value={formData.featuredProductSecondary1Id || ''}
                                onChange={(e) => setFormData({ ...formData, featuredProductSecondary1Id: e.target.value ? parseInt(e.target.value) : undefined })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                            >
                                <option value="">Seleccionar producto</option>
                                {products.map(p => (
                                    <option key={p.id} value={p.id}>{p.name}</option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Producto Secundario 2
                            </label>
                            <select
                                value={formData.featuredProductSecondary2Id || ''}
                                onChange={(e) => setFormData({ ...formData, featuredProductSecondary2Id: e.target.value ? parseInt(e.target.value) : undefined })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                            >
                                <option value="">Seleccionar producto</option>
                                {products.map(p => (
                                    <option key={p.id} value={p.id}>{p.name}</option>
                                ))}
                            </select>
                        </div>
                    </div>
                </div>

                {/* Carrusel */}
                <div className="bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6">
                    <h2 className="text-lg font-semibold text-black dark:text-white mb-4">
                        Carrusel de Productos
                    </h2>

                    <div className="space-y-4">
                        <div className="flex items-center gap-3">
                            <input
                                type="checkbox"
                                id="showCarousel"
                                checked={formData.showCarousel}
                                onChange={(e) => setFormData({ ...formData, showCarousel: e.target.checked })}
                                className="w-4 h-4 rounded border-neutral-300"
                            />
                            <label htmlFor="showCarousel" className="text-sm font-medium text-neutral-700 dark:text-neutral-300">
                                Mostrar carrusel
                            </label>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Título del Carrusel
                            </label>
                            <input
                                type="text"
                                value={formData.carouselTitle || ''}
                                onChange={(e) => setFormData({ ...formData, carouselTitle: e.target.value })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                                placeholder="Productos Destacados"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Productos en el Carrusel ({formData.carouselProductIds?.length || 0} seleccionados)
                            </label>
                            <div className="max-h-48 overflow-y-auto border border-neutral-200 dark:border-neutral-700 rounded-lg">
                                {products.map(p => (
                                    <label
                                        key={p.id}
                                        className="flex items-center gap-3 px-3 py-2 hover:bg-neutral-50 dark:hover:bg-neutral-800 cursor-pointer"
                                    >
                                        <input
                                            type="checkbox"
                                            checked={formData.carouselProductIds?.includes(parseInt(p.id)) || false}
                                            onChange={() => handleCarouselProductToggle(parseInt(p.id))}
                                            className="w-4 h-4 rounded border-neutral-300"
                                        />
                                        <span className="text-sm text-black dark:text-white">{p.name}</span>
                                    </label>
                                ))}
                            </div>
                        </div>
                    </div>
                </div>

                {/* Banner */}
                <div className="lg:col-span-2 bg-white dark:bg-black border border-neutral-200 dark:border-neutral-800 rounded-lg p-6">
                    <h2 className="text-lg font-semibold text-black dark:text-white mb-4">
                        Banner Principal
                    </h2>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                        <div className="flex items-center gap-3">
                            <input
                                type="checkbox"
                                id="bannerEnabled"
                                checked={formData.bannerEnabled}
                                onChange={(e) => setFormData({ ...formData, bannerEnabled: e.target.checked })}
                                className="w-4 h-4 rounded border-neutral-300"
                            />
                            <label htmlFor="bannerEnabled" className="text-sm font-medium text-neutral-700 dark:text-neutral-300">
                                Mostrar banner
                            </label>
                        </div>

                        <div></div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Título del Banner
                            </label>
                            <input
                                type="text"
                                value={formData.bannerTitle || ''}
                                onChange={(e) => setFormData({ ...formData, bannerTitle: e.target.value })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                                placeholder="Título del banner"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Subtítulo del Banner
                            </label>
                            <input
                                type="text"
                                value={formData.bannerSubtitle || ''}
                                onChange={(e) => setFormData({ ...formData, bannerSubtitle: e.target.value })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                                placeholder="Subtítulo del banner"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                URL de Imagen
                            </label>
                            <input
                                type="url"
                                value={formData.bannerImageUrl || ''}
                                onChange={(e) => setFormData({ ...formData, bannerImageUrl: e.target.value })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                                placeholder="https://ejemplo.com/imagen.jpg"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Enlace del Banner
                            </label>
                            <input
                                type="url"
                                value={formData.bannerLink || ''}
                                onChange={(e) => setFormData({ ...formData, bannerLink: e.target.value })}
                                className="w-full px-3 py-2 border border-neutral-300 dark:border-neutral-600 rounded-lg bg-white dark:bg-neutral-900 text-black dark:text-white"
                                placeholder="/search o https://ejemplo.com"
                            />
                        </div>
                    </div>

                    {/* Preview */}
                    {formData.bannerImageUrl && (
                        <div className="mt-4">
                            <label className="block text-sm font-medium text-neutral-700 dark:text-neutral-300 mb-2">
                                Vista previa
                            </label>
                            <div className="relative rounded-lg overflow-hidden h-48 bg-neutral-100 dark:bg-neutral-800">
                                <img
                                    src={formData.bannerImageUrl}
                                    alt="Banner preview"
                                    className="w-full h-full object-cover"
                                    onError={(e) => (e.currentTarget.src = '')}
                                />
                                <div className="absolute inset-0 bg-black/40 flex flex-col justify-center items-center text-white">
                                    <h3 className="text-2xl font-bold">{formData.bannerTitle || 'Título'}</h3>
                                    <p className="text-sm mt-1">{formData.bannerSubtitle || 'Subtítulo'}</p>
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>

            {/* Info de última actualización */}
            {config?.updatedAt && (
                <div className="text-sm text-neutral-500 dark:text-neutral-400 text-right">
                    Última actualización: {new Date(config.updatedAt).toLocaleString()}
                </div>
            )}
        </div>
    );
}

